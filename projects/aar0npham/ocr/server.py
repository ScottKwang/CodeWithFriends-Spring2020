import argparse
import logging
import os

import flask
from flask import Flask, jsonify, request
from werkzeug.utils import secure_filename

from pipeline import serveModel

UPLOAD_FOLDER = f'{os.path.dirname(os.path.relpath(__file__))}/test'
ALLOWED = {'png', 'jpeg', 'jpg'}

app = Flask(__name__)
app.config.from_mapping(SECRET_KEY='development')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


def isAllowed(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED


def getPath(r):
    if 'file' not in r.file:
        logging.warning('does not receive an image')
        return jsonify({'status': 'noInput'}), 403
    file = r.file['file']
    if file.filename == '':
        logging.warning('no input received')
        return jsonify({'status': 'emptyInput'}), 403
    if file and isAllowed(file.filename):
        filename = secure_filename(file.filename)
        fpath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(fpath)
        logging.info(f'got an inputs, saved at {fpath}')
        return fpath
    else:
        logging.error(f'file not accepted, got {file}')
        return jsonify({'status': 'badInput'}), 404


@app.route('/', methods=['GET'])
def isOnline():
    logging.info('ping received')
    return jsonify({'status': 'online'}), 200


# TODO: configure model to run onnx file
# points towards api for inference
@app.route('/api', methods=['POST'])
def parseText():
    fpath = getPath(request)
    results = m.predict(fpath)
    return jsonify({'status': 'OK', 'results': {k: v for k, v in enumerate(results)}}), 200


if __name__ == '__main__':
    logging.info('Starting server...')
    parser = argparse.ArgumentParser()
    parser.add_argument('--docker', action='store_true', help='allows to run on cpu for docker container')
    parser.add_argument('--config', default='config.yml', help='path to config.yml, default is the same dir as pipeline')
    parser.add_argument('--thresh', default=0.7, help='threshold for confidence score')
    opt = parser.parse_args()
    m = serveModel(config_file=opt.config, thresh=opt.thresh, docker=opt.docker)
    app.run(host='0.0.0.0', port=5000)

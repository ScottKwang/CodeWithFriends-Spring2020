# FROM anibali/pytorch:1.5.0-cuda10.2
FROM aar0npham/torch-cv2-docker:latest

MAINTAINER Aaron Pham <aaronpham0103@gmail.com>

USER root
RUN sudo apt-get update && sudo apt-get install -y gcc cmake

WORKDIR /app
COPY requirements.txt requirements.txt 
RUN pip install -r requirements.txt

# folders
COPY modules/ modules/
COPY save_models/ save_models/
COPY tools/ tools/

# files
COPY config.yml config.yml
COPY model.py model.py
COPY net.py net.py
COPY pipeline.py pipeline.py
COPY server.py server.py

# expose port to run the server
EXPOSE 5000
CMD ["python3", "server.py"]

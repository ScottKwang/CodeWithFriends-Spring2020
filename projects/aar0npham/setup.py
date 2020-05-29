import os

from setuptools import find_packages, setup

here = os.path.abspath(os.path.dirname(__file__))

with open(os.path.join(here, 'README.md'), encoding='utf-8') as f:
    long_description = f.read()

setup(
    name='lightlyocr',
    version='1.0',
    description='backend for lightly',
    long_description=long_description,
    long_description_content_type='text/markdown',
    author='Aaron Pham',
    classifiers=[
        'Development Status :: 3 - Alpha',
        'Intended Audience :: Developers',
        'Topic :: Software Development :: Build Tools',
        'License :: OSI Approved :: GNU Affero General License',
        'Programming Language :: Python :: 3.7',
        'Programming Language :: Python :: 3.8',
    ],
    package_dir={'': 'ocr'},
    packages=find_packages(where='ocr'),
    python_requires='>=3.7.*',
)

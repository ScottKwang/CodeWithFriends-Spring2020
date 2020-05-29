#! /bin/bash
source config.env
eval $(find . | grep -E "(__pycache__|\.pyc|\.pyo$)" | xargs rm -rf)
eval $(find . | grep -E "(.ipynb_checkpoints)" | xargs rm -rf)

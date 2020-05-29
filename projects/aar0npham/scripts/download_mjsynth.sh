#!/bin/bash

cd ../ocr/data
if [ ! -f "mjsynth.tar.gz" ]; then
	wget https://www.robots.ox.ac.uk/~vgg/data/text/mjsynth.tar.gz
	tar -xvzf mjsynth.tar.gz
	mv mnt/ramdisk/max/90kDICT32px mjsynth
	rm -r mnt
fi
cd ..

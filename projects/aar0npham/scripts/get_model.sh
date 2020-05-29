#!/bin/bash

cd ocr/pretrained/
# CRNN is a thicc boi
export fileid=1FHCQJcEvNdn92Fg5AflwrjbOgrLSGhAu
export filename=CRNN.pth
wget --save-cookies cookies.txt 'https://docs.google.com/uc?export=download&id='$fileid -O- \
     | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1/p' > confirm.txt

wget --load-cookies cookies.txt -O $filename \
     'https://docs.google.com/uc?export=download&id='$fileid'&confirm='$(<confirm.txt)
rm -f confirm.txt cookies.txt
# CRAFT
wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=13qmxiUESd1hHuZwDUOPvLtC6hMdEzGiY' -O CRAFT.pth

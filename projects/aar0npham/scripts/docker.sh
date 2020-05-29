noneContainter=$(docker images -a | grep "^<none>" | awk '{print $3}')
if [ ! -z "$noneContainter" ]; then
    docker rmi -f $noneContainter
fi
docker build -t aar0npham/lightly-ocr:latest ocr
docker run aar0npham/lightly-ocr:latest 

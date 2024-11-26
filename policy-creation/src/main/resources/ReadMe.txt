Open command prompt and execute the following commands

 1. docker run -d --rm -it -p 4566:4566 -p 4510-4559:4510-4559 localstack/localstack:4.0.2  ==> start the docker with localstack

 2.docker ps  ==> list out running container.

 3.docker exec -it 324c89dcd3e0 bash   ==> Execute/start the container

 4.awslocal sqs create-queue --queue-name my-queue  ===>create queue

 5.awslocal sns create-topic --name my-topic   ===> Create topic

 6.awslocal sns subscribe --topic-arn "arn:aws:sns:us-east-1:000000000000:my-topic" \
    --protocol sqs \
    --notification-endpoint "arn:aws:sqs:us-east-1:000000000000:my-queue" \              ===> Subscribe to topic
    --attributes "{\"RawMessageDelivery\": \"true\"}"

awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name expense-register-new-bill-dlq \
         --attributes '{"VisibilityTimeout":"30","DelaySeconds":"0","ReceiveMessageWaitTimeSeconds":"5","MaximumMessageSize":"262144","MessageRetentionPeriod":"345600"}'

DLQ_ARN=$(awslocal --endpoint-url=http://localhost:4566 sqs get-queue-attributes --attribute-name QueueArn --queue-url=http://localhost:4566/000000000000/expense-register-new-bill-dlq \
|  sed 's/"QueueArn"/\n"QueueArn"/g' | grep '"QueueArn"' | awk -F '"QueueArn":' '{print $2}' | tr -d '"' | xargs)

awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name expense-register-new-bill \
         --attributes '{"VisibilityTimeout":"30","DelaySeconds":"0","ReceiveMessageWaitTimeSeconds":"5","MaximumMessageSize":"262144","MessageRetentionPeriod":"345600","RedrivePolicy":"{\"deadLetterTargetArn\":\"'"$DLQ_ARN"'\",\"maxReceiveCount\":\"3\"}"}'
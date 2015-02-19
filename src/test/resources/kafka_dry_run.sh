#!/bin/bash

# This program should be executed at the head node

zk_conn_str=$(grep zookeeper.connect /opt/kafka/config/client.properties | awk -F= '{print $2}')
broker_conn_str=$(grep metadata.broker.list /opt/kafka/config/client.properties | awk -F= '{print $2}')

function print_usage {
    echo "Usage $0 [consumer | producer | create_topic] <topic>" 
    exit 1
}

function create_topic {
    # note: using replication factor of 1 in sandbox because there is only one broker
    kafka-topics.sh --create --zookeeper ${zk_conn_str} --replication-factor 1 --partitions 1 --topic ${topic}
    echo "current list of topics:"
    kafka-topics.sh --list --zookeeper ${zk_conn_str}
    # echo "topic created with replication factor of 2, try killing a broker and see how at most a single message is lost"
}

function start_producer {
    kafka-console-producer.sh --broker-list ${broker_conn_str} --topic ${topic}
}

function start_consumer {
    kafka-console-consumer.sh --zookeeper ${zk_conn_str} --topic ${topic} --from-beginning
}

####################
# Main
####################
# Check number of arguments 
[ $# -ne 2 ] && print_usage
action=$1
topic=$2

[ ${action} == "producer" ] && start_producer
[ ${action} == "consumer" ] && start_consumer
[ ${action} == "create_topic" ] && create_topic
[ ${action} != "producer" ] && [ ${action} != "consumer" ] && [ ${action} != "create_topic" ] && print_usage



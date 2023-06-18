CREATE TABLE IF NOT EXISTS `MQ`.`event_message`
(
    `oid` bigint
(
    20
) AUTO_INCREMENT,
    `message_id` char
(
    32
) NOT NULL COMMENT 'uuid 32, unequal event message id',
    `event_name` varchar
(
    255
) NOT NULL COMMENT 'event name',
    `biz_key` varchar
(
    400
) NULL COMMENT 'biz key',
    `headers` longtext NOT NULL COMMENT 'event message headers, json text',
    `payload` longtext NOT NULL COMMENT 'event message payload, json text',
    `entry_id` int
(
    11
) DEFAULT NULL,
    `entry_datetime` datetime
(
    3
) NOT NULL DEFAULT CURRENT_TIMESTAMP
(
    3
),
    `h_version` int
(
    11
) NOT NULL DEFAULT '0',
    PRIMARY KEY
(
    `oid`
),
    UNIQUE KEY `event_messageI1`
(
    `message_id`
) COMMENT 'business primary key, corresponds to a user request',
    KEY `event_messageI2`
(
    `event_name`
),
    KEY `event_messageI3`
(
    `biz_key`
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE =utf8mb4_bin COMMENT 'event message, Local event table for Event-driver architecture';
CREATE TABLE CIS.dbo.event_message
(
    oid            bigint IDENTITY,
    message_id     varchar(32)                NOT NULL /**uuid 32, unequal event message id**/,
    event_name     varchar(255)               NOT NULL /** event name**/,
    biz_key        varchar(400) NULL /**biz key**/,
    headers        text                       NOT NULL /**event message headers, json text**/,
    payload        text                       NOT NULL /**event message payload, json text**/,
    entry_id       int      default -1        NOT NULL,
    entry_datetime datetime default getDate() NOT NULL,
    h_version      int      DEFAULT 0         NOT NULL
) WITH identity_gap = 100
CREATE
unique
clustered INDEX event_messageI1 ON CIS.dbo.event_message (oid)
CREATE UNIQUE INDEX event_messageI2 ON CIS.dbo.event_message (message_id)
CREATE INDEX event_messageI3 ON CIS.dbo.event_message (event_name)
CREATE INDEX event_messageI4 ON CIS.dbo.event_message (biz_key)


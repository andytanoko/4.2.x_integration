-- 09 Nov 2009     [Tam Wei Xiang]      #1105      i) Added config_prop for i)tx.delivery.info, ii)tx.delivery.jms, iii) outgoingTransLock
--                                                 iv) incomingTransLock

-- Apply following script to "GTVAN" schema

-- For HTTPBC processBackTxQueue JMS properties
DELETE FROM "config_props" WHERE "category"='tx.delivery.jms';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'destination.jndi', 'queue/gtvan/ishb/processBackTxQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For HTTPBC Tx Delivery Info Props
DELETE FROM "config_props" WHERE "category"='tx.delivery.info';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'max.process.count.percall', '50');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'max.failed.attempts.pertx', '10');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'failed.attempt.alert.threshold', '5');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'tx.delivery.mgr.jndi', 'ISHB_TransactionHandlerBean');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For HTTP BC global locking
DELETE FROM "config_props" WHERE "category"='outgoingTransLock' AND "property_key"='outgoingTransLock';
DELETE FROM "config_props" WHERE "category"='incomingTransLock' AND "property_key"='incomingTransLock';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('outgoingTransLock', 'outgoingTransLock', 'outgoingTransLock');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('incomingTransLock', 'incomingTransLock', 'incomingTransLock');

commit;
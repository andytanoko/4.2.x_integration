use jbossdb;

insert into schedule(target, method_name, method_signature, start_date, period, repetitions)
values('gtvan.httpbc:service=TxDeliveryService', 'processInTx', 'DATE', 'NOW', 60000, -1);

insert into schedule(target, method_name, method_signature, start_date, period, repetitions)
values('gtvan.httpbc:service=TxDeliveryService', 'processOutTx', 'DATE', 'NOW', 60000, -1);

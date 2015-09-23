-- Table: po_item

-- DROP TABLE po_item;

CREATE TABLE po_item
(
  po_item_from_gbusinessid character varying(255),
  po_item_thisdocumentid character varying(255),
  creation_date character varying(255),
  po_lineitem character varying(255),
  prodid_globalpartnerclasscode_6 character varying(255),
  shipping_gshipmenttermcode character varying(255),
  requested_quantity character varying(255),
  shipping_accountid character varying(255),
  shipping_gcarriercode character varying(255),
  shipping_gfobcode character varying(255),
  shipping_servicelevelcode character varying(255),
  shipping_spechandlingtext character varying(255),
  requested_datetimestamp character varying(255),
  requested_gtransporteventcode character varying(255),
  shipto_gbusinessid character varying(255),
  shipto_businessname character varying(255),
  shipto_gpartnerclasscode character varying(255),
  shipto_glocationid character varying(255),
  shipto_addressline1 character varying(255),
  shipto_addressline2 character varying(255),
  shipto_addressline3 character varying(255),
  shipto_cityname character varying(255),
  shipto_countrycode character varying(255),
  shipto_postalcode character varying(255),
  shipto_postoffbox character varying(255),
  shipto_regionname character varying(255),
  acc_bill_globalpartnerclasscode character varying(255),
  uom character varying(255),
  prodid_propproductid_1 character varying(255),
  prodid_propproductid_2 character varying(255),
  prodid_propproductid_3 character varying(255),
  prodid_propproductid_4 character varying(255),
  prodid_propproductid_5 character varying(255),
  prodid_propproductid_6 character varying(255),
  prodid_propproductid_7 character varying(255),
  prodid_propproductid_8 character varying(255),
  prodid_propproductid_9 character varying(255),
  prodid_propproductid_10 character varying(255),
  prodid_globalpartnerclasscode_1 character varying(255),
  prodid_globalpartnerclasscode_2 character varying(255),
  prodid_globalpartnerclasscode_3 character varying(255),
  prodid_globalpartnerclasscode_4 character varying(255),
  prodid_globalpartnerclasscode_5 character varying(255),
  prodid_globalpartnerclasscode_7 character varying(255),
  prodid_globalpartnerclasscode_8 character varying(255),
  prodid_globalpartnerclasscode_9 character varying(255),
  prodid_globalpartnerclasscode_10 character varying(255),
  prod_comments character varying(255),
  requnitprice_currcode character varying(255),
  requnitprice_monetaryamt character varying(255),
  po_delv_from_gbusinessid character varying(255),
  po_delv_thisdocumentid character varying(255),
  create_date character varying(255),
  po_delv_po_lineitem character varying(255),
  sublineitem_linenumber character varying(255),
  isdropship character varying(255),
  totallineamt_currcode character varying(255),
  totallineamt_monetaryamt character varying(255)
) 
WITHOUT OIDS;
ALTER TABLE po_item OWNER TO postgres;





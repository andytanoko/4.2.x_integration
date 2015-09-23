# 08 Sep 2003 I1 v2.1 [Koh Han Sing] Insert Inbound to Outbound Mapping Rule
# 

USE userdb;

INSERT INTO gridtalk_mapping_rule VALUES("-1",
            "GT_GD_INBOUND2OUTBOUND_MR",
            "GridTalk's default header transformation for Inbound to Outbound",
            "",
            "",
            "",
            "",
            "1",
            "0",
            "0",
            "-1",
            "0",
            "1.0");

INSERT INTO mapping_rule VALUES("-1",
            "GT_GD_INBOUND2OUTBOUND_MR",
            "GridTalk's default header transformation for Inbound to Outbound",
            "1",
            "-5",
            "0",
            "",
            "",
            "",
            "0",
            "0",
            "1.0");
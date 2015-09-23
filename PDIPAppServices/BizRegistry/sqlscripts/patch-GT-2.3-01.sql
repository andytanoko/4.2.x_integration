# 19 Jan 2004 Gt 2.3 I1 [Daniel D'Cotta] Update WhitePage.Language constraint

USE appdb;

# RegistryConnectInfo
UPDATE fieldmetainfo SET constraints="type=foreign\r\nforeign.key=languageCode.alpha2Code\r\nforeign.display=languageCode.name\r\nforeign.cached=false\r\n" WHERE label="whitePage.language";

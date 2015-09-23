USE userdb;

# IsSplit=false, IsZip=false
UPDATE channel_info 
SET FlowControlProfile='¨Ì\0sr\03com.gridnode.pdip.app.channel.model.FlowControlInfo«ø%Æ‡V÷‚\0Z\0_isSplitZ\0_isZipI\0\n_splitSizeI\0_splitThresholdI\0\r_zipThresholdxr\04com.gridnode.pdip.framework.db.entity.AbstractEntity`i*‚}y«\0Z\0\n_canDeleteZ\0_isCompleteL\0_currentIdst\0Ljava/util/Vector;xr\0)com.gridnode.pdip.framework.db.DataObjectÂZ‚{ÑUY\0J\0_uIdD\0_versionxp\0\0\0\0\0\0\0\0?\0\0\0\0\0\0\0sr\0java.util.VectorŸó}[Ä;Ø\0I\0capacityIncrementI\0elementCount[\0elementDatat\0[Ljava/lang/Object;xp\0\0\0\0\0\0\0ur\0[Ljava.lang.Object;êŒXüs)l\0\0xp\0\0\0\nsr\0java.lang.Integer‚†§˜Åá8\0I\0valuexr\0java.lang.NumberÜ¨ïî‡ã\0\0xp\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0ppppp\0\0\0\0\0@\0\0\0\0\0'
WHERE TptProtocolType IN ('HTTP','UPC')
;

# IsSplit=false, IsZip=true
UPDATE channel_info
SET FlowControlProfile='¨Ì\0sr\03com.gridnode.pdip.app.channel.model.FlowControlInfo«ø%Æ‡V÷‚\0Z\0_isSplitZ\0_isZipI\0\n_splitSizeI\0_splitThresholdI\0\r_zipThresholdxr\04com.gridnode.pdip.framework.db.entity.AbstractEntity`i*‚}y«\0Z\0\n_canDeleteZ\0_isCompleteL\0_currentIdst\0Ljava/util/Vector;xr\0)com.gridnode.pdip.framework.db.DataObjectÂZ‚{ÑUY\0J\0_uIdD\0_versionxp\0\0\0\0\0\0\0\0?\0\0\0\0\0\0\0sr\0java.util.VectorŸó}[Ä;Ø\0I\0capacityIncrementI\0elementCount[\0elementDatat\0[Ljava/lang/Object;xp\0\0\0\0\0\0\0ur\0[Ljava.lang.Object;êŒXüs)l\0\0xp\0\0\0\nsr\0java.lang.Integer‚†§˜Åá8\0I\0valuexr\0java.lang.NumberÜ¨ïî‡ã\0\0xp\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0ppppp\0\0\0\0@\0\0\0\0\0'
WHERE TptProtocolType='SOAP-HTTP';

# IsSplit=true, IsZip=true
UPDATE channel_info
SET FlowControlProfile='¨Ì\0sr\03com.gridnode.pdip.app.channel.model.FlowControlInfo«ø%Æ‡V÷‚\0Z\0_isSplitZ\0_isZipI\0\n_splitSizeI\0_splitThresholdI\0\r_zipThresholdxr\04com.gridnode.pdip.framework.db.entity.AbstractEntity`i*‚}y«\0Z\0\n_canDeleteZ\0_isCompleteL\0_currentIdst\0Ljava/util/Vector;xr\0)com.gridnode.pdip.framework.db.DataObjectÂZ‚{ÑUY\0J\0_uIdD\0_versionxp\0\0\0\0\0\0\0\0?\0\0\0\0\0\0\0sr\0java.util.VectorŸó}[Ä;Ø\0I\0capacityIncrementI\0elementCount[\0elementDatat\0[Ljava/lang/Object;xp\0\0\0\0\0\0\0ur\0[Ljava.lang.Object;êŒXüs)l\0\0xp\0\0\0\nsr\0java.lang.Integer‚†§˜Åá8\0I\0valuexr\0java.lang.NumberÜ¨ïî‡ã\0\0xp\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0sq\0~\0\n\0\0\0ppppp\0\0\0@\0\0\0\0\0'
WHERE TptProtocolType='JMS' 
;



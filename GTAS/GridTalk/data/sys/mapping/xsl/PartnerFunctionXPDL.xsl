<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="PartnerFunction">
		<xsl:text disable-output-escaping="yes">&#xa;&lt;!DOCTYPE Package SYSTEM "../../validation/dtd/xpdl.dtd"&gt;</xsl:text>
		<Package>
			<xsl:attribute name="Id"><xsl:value-of select="PartnerFunctionId"/></xsl:attribute>
			<xsl:call-template name="PackageHeader"/>
			<xsl:call-template name="RedefinableHeader"/>
			<xsl:call-template name="Applications"/>
			<xsl:call-template name="WorkflowProcesses"/>
		</Package>
	</xsl:template>
	<xsl:template name="PackageHeader">
		<PackageHeader>
			<XPDLVersion/>
			<Vendor/>
			<Created>
				<xsl:call-template name="getCurrentTime">
					<xsl:with-param name="timeformat">yyyy-MM-dd HH:mm:SS</xsl:with-param>
				</xsl:call-template>
			</Created>
		</PackageHeader>
	</xsl:template>
	<xsl:template name="RedefinableHeader">
		<RedefinableHeader>
			<Author>GTAS</Author>
			<Version>
				<xsl:value-of select="Version"/>
			</Version>
		</RedefinableHeader>
	</xsl:template>
	<xsl:template name="Applications">
		<Applications>
			<Application Id="FolderAdapter">
				<Description>Adapter to call java procedure to perform folder exits</Description>
				<FormalParameters>
					<FormalParameter Id="Param1" Index="1" Mode="IN">
						<DataType>
							<DeclaredType Id="java.lang.String"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param2" Index="2" Mode="IN">
						<DataType>
							<DeclaredType Id="java.lang.String"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param3" Index="3" Mode="IN">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param4" Index="4" Mode="OUT">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
				</FormalParameters>
				<ExtendedAttributes>
					<ExtendedAttribute Name="AdapterClass" Value="com.gridnode.gtas.server.document.helpers.FolderDelegate"/>
					<ExtendedAttribute Name="AdapterMethod" Value="exit"/>
				</ExtendedAttributes>
			</Application>
			<Application Id="MappingRuleAdapter">
				<Description>Adapter to call java procedure to perform mapping rule</Description>
				<FormalParameters>
					<FormalParameter Id="Param1" Index="1" Mode="IN">
						<DataType>
							<DeclaredType Id="java.lang.String"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param2" Index="2" Mode="IN">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param3" Index="3" Mode="OUT">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
				</FormalParameters>
				<ExtendedAttributes>
					<ExtendedAttribute Name="AdapterClass" Value="com.gridnode.gtas.server.mapper.helpers.MapperDelegate"/>
					<ExtendedAttribute Name="AdapterMethod" Value="execute"/>
				</ExtendedAttributes>
			</Application>
			<Application Id="UserProcedureAdapter">
				<Description>Adapter to call java procedure to perform mapping rule</Description>
				<FormalParameters>
					<FormalParameter Id="Param1" Index="1" Mode="IN">
						<DataType>
							<DeclaredType Id="java.lang.String"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param2" Index="2" Mode="IN">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param3" Index="3" Mode="OUT">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
				</FormalParameters>
				<ExtendedAttributes>
					<ExtendedAttribute Name="AdapterClass" Value="com.gridnode.gtas.server.userprocedure.helpers.UserProcedureDelegate"/>
					<ExtendedAttribute Name="AdapterMethod" Value="execute"/>
				</ExtendedAttributes>
			</Application>
			<Application Id="AlertAdapter">
				<Description>Adapter to call java procedure to raise alert</Description>
				<FormalParameters>
					<FormalParameter Id="Param1" Index="1" Mode="IN">
						<DataType>
							<DeclaredType Id="java.lang.String"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param2" Index="2" Mode="IN">
						<DataType>
							<DeclaredType Id="java.lang.String"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param3" Index="3" Mode="IN">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
					<FormalParameter Id="Param4" Index="4" Mode="OUT">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
				</FormalParameters>
				<ExtendedAttributes>
					<ExtendedAttribute Name="AdapterClass" Value="com.gridnode.gtas.server.docalert.helpers.RaiseAlertDelegate"/>
					<ExtendedAttribute Name="AdapterMethod" Value="raiseAlert"/>
				</ExtendedAttributes>
			</Application>
		</Applications>
	</xsl:template>
	<xsl:template name="WorkflowProcesses">
		<WorkflowProcesses>
			<WorkflowProcess>
				<xsl:attribute name="Id"><xsl:value-of select="PartnerFunctionId"/></xsl:attribute>
				<ProcessHeader/>
				<FormalParameters>
					<FormalParameter Id="main.gdocs" Index="1" Mode="IN">
						<DataType>
							<DeclaredType Id="java.util.Collection"/>
						</DataType>
					</FormalParameter>
				</FormalParameters>
				<Activities>
					<Activity Id="START">
						<Description>Start of Process</Description>
						<Implementation>
							<No/>
						</Implementation>
						<StartMode>
							<Automatic/>
						</StartMode>
						<FinishMode>
							<Automatic/>
						</FinishMode>
						<xsl:if test="(WorkflowActivity[1]/ActivityType='2' or WorkflowActivity[1]/ActivityType='3' or WorkflowActivity[1]/ActivityType='4') and (count(//WorkflowActivity[(preceding-sibling::WorkflowActivity[1]/ActivityType='5')]) &gt; '0')">
							<TransitionRestrictions>
								<TransitionRestriction>
									<Split>
										<xsl:attribute name="Type">AND</xsl:attribute>
										<TransitionRefs>
											<xsl:for-each select="//WorkflowActivity">
												<xsl:if test="(preceding-sibling::WorkflowActivity[1]/ActivityType='5') or (position() = '1')">
													<TransitionRef>
														<xsl:attribute name="Id">
															<xsl:text>START_</xsl:text>
															<xsl:choose>
																<xsl:when test="ActivityType='0'">MappingRule<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='1'">UserProcedure<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='2'">ExitToImport<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='3'">ExitToOutbound<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='4'">ExitToExport<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='5'">ExitPoint</xsl:when>
																<xsl:when test="ActivityType='6'">ExitToPort<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='7'">SaveToSystemFolder<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='8'">ExitToChannel<xsl:value-of select="position()"/></xsl:when>
																<xsl:when test="ActivityType='9'">RaiseAlert<xsl:value-of select="position()"/></xsl:when>
															</xsl:choose>
														</xsl:attribute>
													</TransitionRef>
												</xsl:if>
											</xsl:for-each>
										</TransitionRefs>
									</Split>
								</TransitionRestriction>
							</TransitionRestrictions>
						</xsl:if>
					</Activity>
					<Activity Id="END">
						<Description>End of Process</Description>
						<Implementation>
							<No/>
						</Implementation>
						<StartMode>
							<Automatic/>
						</StartMode>
						<FinishMode>
							<Automatic/>
						</FinishMode>
					</Activity>
					<Activity Id="ExitPoint">
						<Description>Exit Point of All Activities</Description>
						<Implementation>
							<Tool Id="FolderAdapter" Type="APPLICATION">
								<ActualParameters>
									<ActualParameter>exit</ActualParameter>
									<ActualParameter>param</ActualParameter>
									<ActualParameter>main.gdocs</ActualParameter>
									<ActualParameter>main.gdocs</ActualParameter>
								</ActualParameters>
								<ExtendedAttributes>
									<ExtendedAttribute Name="exit" Value="99"/>
									<ExtendedAttribute Name="param" Value=""/>
								</ExtendedAttributes>
							</Tool>
						</Implementation>
						<StartMode>
							<Automatic/>
						</StartMode>
						<FinishMode>
							<Automatic/>
						</FinishMode>
						<TransitionRestrictions>
							<TransitionRestriction>
								<Join Type="AND"/>
							</TransitionRestriction>
						</TransitionRestrictions>
					</Activity>
					<xsl:for-each select="WorkflowActivity">
						<!--xsl:if test="not(ActivityType='5' and position()=last() and (count(//WorkflowActivity[ActivityType='5']) &gt; count(//WorkflowActivity[ActivityType='2' or ActivityType='3' or ActivityType='4'])))"-->
						<xsl:if test="not(ActivityType='5' and (count(//WorkflowActivity[ActivityType='5']) &gt; count(//WorkflowActivity[ActivityType='2' or ActivityType='3' or ActivityType='4'])))">
							<xsl:call-template name="Activity"/>
						</xsl:if>
					</xsl:for-each>
				</Activities>
				<Transitions>
					<xsl:for-each select="WorkflowActivity">
						<xsl:call-template name="Transition"/>
					</xsl:for-each>
					<Transition>
						<xsl:attribute name="Id">ExitPoint_END</xsl:attribute>
						<xsl:attribute name="From">ExitPoint</xsl:attribute>
						<xsl:attribute name="To">END</xsl:attribute>
					</Transition>
				</Transitions>
			</WorkflowProcess>
		</WorkflowProcesses>
	</xsl:template>
	<xsl:template name="Activity">
		<xsl:variable name="CURRENT" select="current()"/>
		<xsl:variable name="CURRENTPOS" select="position()"/>
		<Activity>
			<xsl:choose>
				<xsl:when test="ActivityType='0'">
					<xsl:attribute name="Id">MappingRule<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='1'">
					<xsl:attribute name="Id">UserProcedure<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='2'">
					<xsl:attribute name="Id">ExitToImport<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='3'">
					<xsl:attribute name="Id">ExitToOutbound<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='4'">
					<xsl:attribute name="Id">ExitToExport<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='5'">
					<xsl:attribute name="Id">ExitWorkflow<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='6'">
					<xsl:attribute name="Id">ExitToPort<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='7'">
					<xsl:attribute name="Id">SaveToSystemFolder<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='8'">
					<xsl:attribute name="Id">ExitToChannel<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="ActivityType='9'">
					<xsl:attribute name="Id">RaiseAlert<xsl:value-of select="position()"/></xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<Description>
				<xsl:value-of select="Description"/>
			</Description>
			<Implementation>
				<xsl:variable name="FORK" select="//WorkflowActivity[(ActivityType='2' or ActivityType='3' or ActivityType='4') and (position() &lt; ($CURRENTPOS + 1))]"/>
				<xsl:choose>
					<xsl:when test="ActivityType='0'">
						<Tool Id="MappingRuleAdapter" Type="APPLICATION">
							<ActualParameters>
								<ActualParameter>uid</ActualParameter>
								<xsl:if test="count($FORK)=0">
									<ActualParameter>main.gdocs</ActualParameter>
									<ActualParameter>main.gdocs</ActualParameter>
								</xsl:if>
								<xsl:if test="count($FORK) &gt; 0">
									<xsl:if test="preceding-sibling::WorkflowActivity[1]/ActivityType='5'">
										<ActualParameter>main.gdocs</ActualParameter>
									</xsl:if>
									<xsl:if test="preceding-sibling::WorkflowActivity[1]/ActivityType!='5'">
										<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
									</xsl:if>
									<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
								</xsl:if>
							</ActualParameters>
							<ExtendedAttributes>
								<ExtendedAttribute>
									<xsl:attribute name="Name">uid</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="Param"/></xsl:attribute>
								</ExtendedAttribute>
							</ExtendedAttributes>
						</Tool>
					</xsl:when>
					<xsl:when test="ActivityType='1'">
						<Tool Id="UserProcedureAdapter" Type="APPLICATION">
							<ActualParameters>
								<ActualParameter>uid</ActualParameter>
								<xsl:if test="count($FORK)=0">
									<ActualParameter>main.gdocs</ActualParameter>
									<ActualParameter>main.gdocs</ActualParameter>
								</xsl:if>
								<xsl:if test="count($FORK) &gt; 0">
									<xsl:if test="preceding-sibling::WorkflowActivity[1]/ActivityType='5'">
										<ActualParameter>main.gdocs</ActualParameter>
									</xsl:if>
									<xsl:if test="preceding-sibling::WorkflowActivity[1]/ActivityType!='5'">
										<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
									</xsl:if>
									<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
								</xsl:if>
							</ActualParameters>
							<ExtendedAttributes>
								<ExtendedAttribute>
									<xsl:attribute name="Name">uid</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="Param"/></xsl:attribute>
								</ExtendedAttribute>
							</ExtendedAttributes>
						</Tool>
					</xsl:when>
					<xsl:when test="ActivityType='2' or ActivityType='3' or ActivityType='4'">
						<Tool Id="FolderAdapter" Type="APPLICATION">
							<ActualParameters>
								<ActualParameter>exit</ActualParameter>
								<ActualParameter>param</ActualParameter>
								<ActualParameter>main.gdocs</ActualParameter>
								<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
							</ActualParameters>
							<ExtendedAttributes>
								<ExtendedAttribute>
									<xsl:attribute name="Name">exit</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="ActivityType"/></xsl:attribute>
								</ExtendedAttribute>
								<ExtendedAttribute>
									<xsl:attribute name="Name">param</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="Param"/></xsl:attribute>
								</ExtendedAttribute>
							</ExtendedAttributes>
						</Tool>
					</xsl:when>
					<xsl:when test="ActivityType='5'">
						<Tool Id="FolderAdapter" Type="APPLICATION">
							<ActualParameters>
								<ActualParameter>exit</ActualParameter>
								<ActualParameter>param</ActualParameter>
								<xsl:if test="count($FORK) &gt; 0">
									<xsl:choose>
										<xsl:when test="ActivityType='6' or ActivityType='8'">
											<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
											<ActualParameter>fork<xsl:value-of select="count($FORK)+1"/>.gdocs</ActualParameter>
										</xsl:when>
										<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='5'">
											<ActualParameter>main.gdocs</ActualParameter>
											<ActualParameter>fork<xsl:value-of select="count($FORK)+1"/>.gdocs</ActualParameter>
										</xsl:when>
										<xsl:otherwise>
											<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
											<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
								<xsl:if test="count($FORK)=0">
									<ActualParameter>main.gdocs</ActualParameter>
									<ActualParameter>main.gdocs</ActualParameter>
								</xsl:if>
							</ActualParameters>
							<ExtendedAttributes>
								<ExtendedAttribute>
									<xsl:attribute name="Name">exit</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="ActivityType"/></xsl:attribute>
								</ExtendedAttribute>
								<ExtendedAttribute>
									<xsl:attribute name="Name">param</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="Param"/></xsl:attribute>
								</ExtendedAttribute>
							</ExtendedAttributes>
						</Tool>
					</xsl:when>
					<!-- Modified By Koh Han Sing on 2003-06-03 To pass down the GridDocument even if exit to port, etc -->
					<xsl:when test="ActivityType='6' or ActivityType='7' or ActivityType='8'">
						<Tool Id="FolderAdapter" Type="APPLICATION">
							<ActualParameters>
								<ActualParameter>exit</ActualParameter>
								<ActualParameter>param</ActualParameter>
								<xsl:if test="count($FORK) &gt; 0">
									<xsl:choose>
										<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='5'">
											<ActualParameter>main.gdocs</ActualParameter>
											<ActualParameter>fork<xsl:value-of select="count($FORK)+1"/>.gdocs</ActualParameter>
										</xsl:when>
										<xsl:otherwise>
											<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
											<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
								<xsl:if test="count($FORK)=0">
									<ActualParameter>main.gdocs</ActualParameter>
									<ActualParameter>main.gdocs</ActualParameter>
								</xsl:if>
							</ActualParameters>
							<ExtendedAttributes>
								<ExtendedAttribute>
									<xsl:attribute name="Name">exit</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="ActivityType"/></xsl:attribute>
								</ExtendedAttribute>
								<ExtendedAttribute>
									<xsl:attribute name="Name">param</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="Param"/></xsl:attribute>
								</ExtendedAttribute>
							</ExtendedAttributes>
						</Tool>
					</xsl:when>
					<xsl:when test="ActivityType='9'">
						<Tool Id="AlertAdapter" Type="APPLICATION">
							<ActualParameters>
								<ActualParameter>alert.type</ActualParameter>
								<ActualParameter>userdefined.alert</ActualParameter>
								<xsl:if test="count($FORK)=0">
									<ActualParameter>main.gdocs</ActualParameter>
									<ActualParameter>main.gdocs</ActualParameter>
								</xsl:if>
								<xsl:if test="count($FORK) &gt; 0">
									<xsl:if test="preceding-sibling::WorkflowActivity[1]/ActivityType='5'">
										<ActualParameter>main.gdocs</ActualParameter>
									</xsl:if>
									<xsl:if test="preceding-sibling::WorkflowActivity[1]/ActivityType!='5'">
										<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
									</xsl:if>
									<ActualParameter>fork<xsl:value-of select="count($FORK)"/>.gdocs</ActualParameter>
								</xsl:if>
							</ActualParameters>
							<ExtendedAttributes>
								<ExtendedAttribute>
									<xsl:attribute name="Name">alert.type</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="Param[1]"/></xsl:attribute>
								</ExtendedAttribute>
								<ExtendedAttribute>
									<xsl:attribute name="Name">userdefined.alert</xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of select="Param[2]"/></xsl:attribute>
								</ExtendedAttribute>
							</ExtendedAttributes>
						</Tool>
					</xsl:when>
				</xsl:choose>
			</Implementation>
			<StartMode>
				<Automatic/>
			</StartMode>
			<FinishMode>
				<Automatic/>
			</FinishMode>
			<xsl:if test="((following-sibling::WorkflowActivity[1]/ActivityType='2' or following-sibling::WorkflowActivity[1]/ActivityType='3' or following-sibling::WorkflowActivity[1]/ActivityType='4') and (ActivityType!='5'))">
				<TransitionRestrictions>
					<TransitionRestriction>
						<Split>
							<xsl:attribute name="Type">AND</xsl:attribute>
							<TransitionRefs>
								<TransitionRef>
									<xsl:attribute name="Id">
										<xsl:choose>
											<xsl:when test="ActivityType='0'">MappingRule<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='1'">UserProcedure<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='2'">ExitToImport<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='3'">ExitToOutbound<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='4'">ExitToExport<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='5'">ExitWorkflow<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='6'">ExitToPort<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='7'">SaveToSystemFolder<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='8'">ExitToChannel<xsl:value-of select="position()"/></xsl:when>
											<xsl:when test="ActivityType='9'">RaiseAlert<xsl:value-of select="position()"/></xsl:when>
										</xsl:choose>
										<xsl:text>_</xsl:text>
										<xsl:choose>
											<xsl:when test="following-sibling::WorkflowActivity[1]/ActivityType='2'">ExitToImport<xsl:value-of select="position()+1"/></xsl:when>
											<xsl:when test="following-sibling::WorkflowActivity[1]/ActivityType='3'">ExitToOutbound<xsl:value-of select="position()+1"/></xsl:when>
											<xsl:when test="following-sibling::WorkflowActivity[1]/ActivityType='4'">ExitToExport<xsl:value-of select="position()+1"/></xsl:when>
										</xsl:choose>
									</xsl:attribute>
								</TransitionRef>
								<xsl:variable name="NEWFLOW" select="//WorkflowActivity[preceding-sibling::WorkflowActivity[1]/ActivityType='5']"/>
								<xsl:for-each select="$NEWFLOW">
									<xsl:variable name="THISFLOW" select="."/>
									<TransitionRef>
										<xsl:attribute name="Id">
											<xsl:choose>
												<xsl:when test="$CURRENT/ActivityType='0'">MappingRule<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='1'">UserProcedure<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='2'">ExitToImport<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='3'">ExitToOutbound<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='4'">ExitToExport<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='5'">ExitWorkflow<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='6'">ExitToPort<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='7'">SaveToSystemFolder<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='8'">ExitToChannel<xsl:value-of select="$CURRENTPOS"/></xsl:when>
												<xsl:when test="$CURRENT/ActivityType='9'">RaiseAlert<xsl:value-of select="$CURRENTPOS"/></xsl:when>
											</xsl:choose>
											<xsl:text>_</xsl:text>
											<!--xsl:choose>
												<xsl:when test="$THISFLOW/ActivityType='5'">
													<xsl:for-each select="//WorkflowActivity"-->
														<!--xsl:if test="position()=last()">ExitWorkflow<xsl:value-of select="position()"/></xsl:if-->
														<!--xsl:if test="position()=last()">ExitPoint</xsl:if>
													</xsl:for-each>
												</xsl:when>
												<xsl:otherwise-->
													<xsl:for-each select="//WorkflowActivity">
														<xsl:variable name="THIS" select="."/>
														<xsl:if test="$THIS=$THISFLOW">
															<xsl:if test="($THISFLOW/ActivityType='0' or $THISFLOW/ActivityType='1' or $THISFLOW/ActivityType='6' or $THISFLOW/ActivityType='7' or $THISFLOW/ActivityType='8' or $THISFLOW/ActivityType='9') and (preceding-sibling::WorkflowActivity[1]/ActivityType='5')">
																<xsl:choose>
																	<xsl:when test="ActivityType='0'">MappingRule<xsl:value-of select="position()"/></xsl:when>
																	<xsl:when test="ActivityType='1'">UserProcedure<xsl:value-of select="position()"/></xsl:when>
																	<xsl:when test="ActivityType='6'">ExitToPort<xsl:value-of select="position()"/></xsl:when>
																	<xsl:when test="ActivityType='7'">SaveToSystemFolder<xsl:value-of select="position()"/></xsl:when>
																	<xsl:when test="ActivityType='8'">ExitToChannel<xsl:value-of select="position()"/></xsl:when>
																	<xsl:when test="ActivityType='9'">RaiseAlert<xsl:value-of select="position()"/></xsl:when>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="$THISFLOW/ActivityType='2' or $THISFLOW/ActivityType='3' or $THISFLOW/ActivityType='4'">
																<xsl:choose>
																	<xsl:when test="ActivityType='2'">ExitToImport<xsl:value-of select="position()"/></xsl:when>
																	<xsl:when test="ActivityType='3'">ExitToOutbound<xsl:value-of select="position()"/></xsl:when>
																	<xsl:when test="ActivityType='4'">ExitToExport<xsl:value-of select="position()"/></xsl:when>
																</xsl:choose>
															</xsl:if>
														</xsl:if>
													</xsl:for-each>
												<!--/xsl:otherwise>
											</xsl:choose-->
										</xsl:attribute>
									</TransitionRef>
								</xsl:for-each>
							</TransitionRefs>
						</Split>
					</TransitionRestriction>
				</TransitionRestrictions>
			</xsl:if>
			<ExtendedAttributes>
				<xsl:for-each select="./Param">
					<xsl:call-template name="SuspendActivityAttributes"/>
				</xsl:for-each>
			</ExtendedAttributes>
		</Activity>
	</xsl:template>
	<xsl:template name="Transition">
		<xsl:variable name="CURRENT" select="current()"/>
		<xsl:variable name="CURRENTPOS" select="position()"/>
		<Transition>
			<xsl:choose>
				<xsl:when test="$CURRENTPOS=1 or (preceding-sibling::WorkflowActivity[1]/ActivityType='5' and (//WorkflowActivity[1]/ActivityType='2' or //WorkflowActivity[1]/ActivityType='3' or //WorkflowActivity[1]/ActivityType='4'))">
					<xsl:variable name="FROM">START</xsl:variable>
					<xsl:variable name="TO">
						<xsl:choose>
							<xsl:when test="ActivityType='0'">
								<xsl:text>MappingRule</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='1'">
								<xsl:text>UserProcedure</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='2'">
								<xsl:text>ExitToImport</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='3'">
								<xsl:text>ExitToOutbound</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='4'">
								<xsl:text>ExitToExport</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='5'">
								<xsl:text>ExitPoint</xsl:text>
							</xsl:when>
							<xsl:when test="ActivityType='6'">
								<xsl:text>ExitToPort</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='7'">
								<xsl:text>SaveToSystemFolder</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='8'">
								<xsl:text>ExitToChannel</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='9'">
								<xsl:text>RaiseAlert</xsl:text>
								<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
						</xsl:choose>
					</xsl:variable>
					<xsl:attribute name="Id"><xsl:value-of select="$FROM"/>_<xsl:value-of select="$TO"/></xsl:attribute>
					<xsl:attribute name="From"><xsl:value-of select="$FROM"/></xsl:attribute>
					<xsl:attribute name="To"><xsl:value-of select="$TO"/></xsl:attribute>
				</xsl:when>
				<!-- Link the activity before the exitToFolder to the activity after the exitWorkflow -->
				<xsl:when test="(preceding-sibling::WorkflowActivity[1]/ActivityType='5') and (//WorkflowActivity[1]/ActivityType='0' or //WorkflowActivity[1]/ActivityType='1' or //WorkflowActivity[1]/ActivityType='7' or //WorkflowActivity[1]/ActivityType='9')">
					<xsl:variable name="FROM">
						<xsl:for-each select="//WorkflowActivity">
							<xsl:if test="((following-sibling::WorkflowActivity[1][ActivityType='2' or ActivityType='3' or ActivityType='4']) and (ActivityType='0' or ActivityType='1' or ActivityType='7' or ActivityType='9'))">
								<xsl:choose>
									<xsl:when test="ActivityType='0'">
										<xsl:text>MappingRule</xsl:text>
									</xsl:when>
									<xsl:when test="ActivityType='1'">
										<xsl:text>UserProcedure</xsl:text>
									</xsl:when>
									<xsl:when test="ActivityType='7'">
										<xsl:text>SaveToSystemFolder</xsl:text>
									</xsl:when>
									<xsl:when test="ActivityType='9'">
										<xsl:text>RaiseAlert</xsl:text>
									</xsl:when>
								</xsl:choose>
								<xsl:value-of select="position()"/>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					<xsl:variable name="TO">
						<xsl:choose>
							<xsl:when test="ActivityType='0'">
								<xsl:text>MappingRule</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='1'">
								<xsl:text>UserProcedure</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='2'">
								<xsl:text>ExitToImport</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='3'">
								<xsl:text>ExitToOutbound</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='4'">
								<xsl:text>ExitToExport</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<!--xsl:when test="ActivityType='5' and position()=last()">
								<xsl:text>ExitPoint</xsl:text>
							</xsl:when-->
							<xsl:when test="ActivityType='5'">
								<xsl:text>ExitWorkflow</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='6'">
								<xsl:text>ExitToPort</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='7'">
								<xsl:text>SaveToSystemFolder</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='8'">
								<xsl:text>ExitToChannel</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='9'">
								<xsl:text>RaiseAlert</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
						</xsl:choose>
					</xsl:variable>
					<xsl:attribute name="Id"><xsl:value-of select="$FROM"/>_<xsl:value-of select="$TO"/></xsl:attribute>
					<xsl:attribute name="From"><xsl:value-of select="$FROM"/></xsl:attribute>
					<xsl:attribute name="To"><xsl:value-of select="$TO"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="FROM">
						<xsl:choose>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='0'">
								<xsl:text>MappingRule</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='1'">
								<xsl:text>UserProcedure</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='2'">
								<xsl:text>ExitToImport</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='3'">
								<xsl:text>ExitToOutbound</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='4'">
								<xsl:text>ExitToExport</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='5'">
								<xsl:text>ExitWorkflow</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='6'">
								<xsl:text>ExitToPort</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='7'">
								<xsl:text>SaveToSystemFolder</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='8'">
								<xsl:text>ExitToChannel</xsl:text>
							</xsl:when>
							<xsl:when test="preceding-sibling::WorkflowActivity[1]/ActivityType='9'">
								<xsl:text>RaiseAlert</xsl:text>
							</xsl:when>
						</xsl:choose>
						<xsl:value-of select="$CURRENTPOS - 1"/>
					</xsl:variable>
					<xsl:variable name="TO">
						<xsl:choose>
							<xsl:when test="ActivityType='0'">
								<xsl:text>MappingRule</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='1'">
								<xsl:text>UserProcedure</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='2'">
								<xsl:text>ExitToImport</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='3'">
								<xsl:text>ExitToOutbound</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='4'">
								<xsl:text>ExitToExport</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<!--xsl:when test="ActivityType='5' and position()=last()">
								<xsl:text>ExitPoint</xsl:text>
							</xsl:when-->
							<xsl:when test="ActivityType='5'">
								<xsl:text>ExitWorkflow</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='6'">
								<xsl:text>ExitToPort</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='7'">
								<xsl:text>SaveToSystemFolder</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='8'">
								<xsl:text>ExitToChannel</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
							<xsl:when test="ActivityType='9'">
								<xsl:text>RaiseAlert</xsl:text>
						<xsl:value-of select="$CURRENTPOS"/>
							</xsl:when>
						</xsl:choose>
					</xsl:variable>
					<xsl:attribute name="Id"><xsl:value-of select="$FROM"/>_<xsl:value-of select="$TO"/></xsl:attribute>
					<xsl:attribute name="From"><xsl:value-of select="$FROM"/></xsl:attribute>
					<xsl:attribute name="To"><xsl:value-of select="$TO"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		</Transition>
		<!--xsl:if test="(ActivityType='5') and ($CURRENTPOS!=last())"-->
		<xsl:if test="(ActivityType='5')">
			<Transition>
				<xsl:variable name="FROM">
					<xsl:text>ExitWorkflow</xsl:text>
					<xsl:value-of select="$CURRENTPOS"/>
				</xsl:variable>
				<xsl:variable name="TO">ExitPoint</xsl:variable>
				<xsl:attribute name="Id"><xsl:value-of select="$FROM"/>_<xsl:value-of select="$TO"/></xsl:attribute>
				<xsl:attribute name="From"><xsl:value-of select="$FROM"/></xsl:attribute>
				<xsl:attribute name="To"><xsl:value-of select="$TO"/></xsl:attribute>
			</Transition>
		</xsl:if>
	</xsl:template>
	<xsl:template name="getCurrentTime">
		<xsl:param name="timeformat"/>
		<xsl:variable name="nowSimpleDate" select="simpleDateFormat:new($timeformat)" xmlns:simpleDateFormat="/java.text.SimpleDateFormat"/>
		<xsl:variable name="nowDate" select="date:new()" xmlns:date="/java.util.Date"/>
		<xsl:variable name="now" select="simpleDateFormat:format($nowSimpleDate, $nowDate)
		" xmlns:simpleDateFormat="/java.text.SimpleDateFormat"/>
		<xsl:value-of select="$now"/>
	</xsl:template>

	<xsl:template name="SuspendActivityAttributes">
		<xsl:if test=". = 'SUSPEND_ACTIVITY'">
			<xsl:variable name="index" select="position()"/>
				<ExtendedAttribute>
					<xsl:attribute name="Name">wf.dispatchInterval</xsl:attribute>
					<xsl:attribute name="Value"><xsl:value-of select="../Param[$index + 1]"/></xsl:attribute>
				</ExtendedAttribute>
				<ExtendedAttribute>
					<xsl:attribute name="Name">wf.dispatchCount</xsl:attribute>
					<xsl:attribute name="Value"><xsl:value-of select="../Param[$index + 2]"/></xsl:attribute>
				</ExtendedAttribute>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>

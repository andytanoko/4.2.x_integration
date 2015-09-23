<%@page import="com.gridnode.pdip.base.docservice.util.DocumentServiceUtil,
                com.gridnode.pdip.base.docservice.filesystem.tree.*,
                com.gridnode.pdip.base.docservice.view.*,
                java.util.*" %>

<HTML>
    <HEAD>
        <TITLE>
            Search Tree
        </TITLE>

        <link rel="stylesheet" type="text/css" href="../css/style.css">

        <script language="Javascript" type="text/javascript" src="../javascript/ecms_system.js"></script>
        <script language="Javascript">
            function doAction(path, type, id)
            {
                opener.setUpPath(path, type, id);
                window.close();
            }
            
            function displayError(filter)
            {
                if (filter == 'file')
                {
                    alert("Select a file!");
                }
                else if (filter == 'folder')
                {
                    alert("Select a folder!");
                }
                else if (filter == 'document')
                {
                    alert("Select a document!");
                }
            }
        </script>

        <style type="text/css">
            <!--
            .bullet { font-weight: normal;}
            .collapsed { background-color: white; font-weight: bold; }
            .expanded { font-weight: bold; font-color:yellow;}
            LI.bullet, LI.expanded { font-weight: bold; list-style-type: circle; margin-left: -20px; }
            LI.expanded { list-style-image: url(expanded.gif);}
            LI.item { list-style-image: url(bullet.gif);}
            -->
        </style>

        <script language="JavaScript">

            ECMS_class_bullet = 'bullet';
            ECMS_class_collapsed = 'collapsed';
            ECMS_class_expanded = 'expanded';


            ECMS_image_bullet.src = '../javascript/bullet.gif';
            ECMS_image_collapsed.src = '../javascript/collapsed.gif';
            ECMS_image_expanded.src = '../javascript/expanded.gif';


            ECMS_image_bullet.alt = '';
            ECMS_image_collapsed.alt = '';
            ECMS_image_expanded.alt = 'menu (hide)';

            ECMS_menu_indent = 15;

            ECMS_status_display = false;

            ECMS_repeat_links = true;

            ECMS_collapse_on_expand = false;

            ECMS_auto_expand = false;
            ECMS_auto_expand_delay = 750;


            /*
            BUILD THE MENU
            ECMS_add_item(level, name, url, target, expanded, title);
            - level:        level of the item (0 is a top level menu choice, 1 under that, 2 under that)
            - name:         text displayed for the item (HTML markup allowed)
            - url:          link for this item
            - target:       target frame for link
            - expanded:     true/false (initial expanded?  false is default)
            - title:        title for link (often appears as a tooltip on the link)
            */


            <%
                TreeView treeView = null;
                String filter = request.getParameter("filter");
                if (filter == null)
                {
                    filter = "file";
                    treeView = DocumentServiceUtil.getTreeView(TreeView.FILE_FILTER);
                }
                else if (filter.equals("folder"))
                {
                    treeView = DocumentServiceUtil.getTreeView(TreeView.FOLDER_FILTER);
                }
                else if (filter.equals("document"))
                {
                    treeView = DocumentServiceUtil.getTreeView(TreeView.DOCUMENT_FILTER);
                }
                else 
                {
                    filter = "file";
                    treeView = DocumentServiceUtil.getTreeView(TreeView.FILE_FILTER);
                }
            %>

            ECMS_add_item(0, '<img src=../javascript/root.gif border=0 />&nbsp;<%= treeView.getName() %>', '', '', true);

            <% 
                String jscriptError = "javascript:displayError(\'" + filter + "\');";
                while (treeView.hasNextNode())
                {
                    DocumentTreeNode treeNode = (DocumentTreeNode) treeView.getNextNode();
                    String jscript = jscriptError;
                    String link = treeNode.getName();
                    String jscriptSubmit = "javascript:doAction(\'" + treeNode.getCanonicalPath() + 
                        "\', \'" + treeNode.getType() + "\', \'" + treeNode.getId() + "\');";
                    if (treeNode.getType() == DocumentTreeNode.DOMAIN_NODE)
                    {
                        link = "<img src=../javascript/domain.gif border=0 />&nbsp;" + link;
                    }
                    else if (treeNode.getType() == DocumentTreeNode.FOLDER_NODE)
                    {
                        if (filter.equals("folder"))
                        {
                            jscript = jscriptSubmit;
                        }
                        link = "<img src=../javascript/folder.gif border=0 />&nbsp;" + link;
                    }
                    if (treeNode.getType() == DocumentTreeNode.DOCUMENT_NODE)
                    {
                        if (filter.equals("document"))
                        {
                            jscript = jscriptSubmit;
                        }
                        link = "<img src=../javascript/document.gif border=0 />&nbsp;" + link;
                    }
                    if (treeNode.getType() == DocumentTreeNode.FILE_NODE)
                    {
                        if (filter.equals("file"))
                        {
                            jscript = jscriptSubmit;
                        }
                        link = "<img src=../javascript/file.gif border=0 />&nbsp;" + link;
                    }
            %>

            ECMS_add_item('<%= treeNode.getLevel() %>',
                          '<%= link %>',
                          "<%= jscript %>", 
                          '');

            <% 
                } 
            %>
            ECMS_end_menu();
        </script>
    </HEAD>

</HTML>

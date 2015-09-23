/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NavPageRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-10     Andrew Hill         Created (based on old NavigationRenderer)
 * 2003-03-17     Andrew Hill         Support for initiallyExpanded & lots of comments
 * 2003-04-08     Andrew Hill         Use IGlobals script url constant
 * 2003-06-17     Andrew Hill         Support for Insert
 * 2003-06-24     Andrew Hill         Fix support for customised navtree icon rendering
 * 2005-03-15     Andrew Hill         Support the requiresAdmin property in navlinks
 * 2006-04-24     Neo Sok Lay         Support the requiresP2P and requiresUDDI property in navlinks
 */
package com.gridnode.gtas.client.web.navigation;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.IdentifiedBean;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.bp.ProcessInstanceNavRenderer;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.AnchorConversionRenderer;
import com.gridnode.gtas.client.web.renderers.INodeTypes;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.xml.IDocumentManager;

public class NavPageRenderer extends AbstractRenderer
{
  public static final String NAV_MAPPING_PARAMETER = NavPageAction.NAV_MAPPING_PARAMETER;
  public static final String NAV_GROUP_PARAMETER = NavPageAction.NAV_GROUP_PARAMETER;

  static final String NAVPAGE_MAPPING = "navPage"; //20030310AH

  //private static final String CONTENT_ID = "navigator_content";
  //private static final String INSERT_HEADER = "insert_header";
  //private static final String INSERT_NAVIGATOR = "insert_navigator";

  private Navgroup _navgroup; //20030310AH
  //private ActionMapping _actionMapping; //20030310AH

  private static final int NAVTREE_SETUP_BUFFER_SIZE = 512; //20030317AH - Initial size of navTree setupBuffer StringBuffer

  //20030304AH - Paths to tree icons & style class names
  //nb: there is also a javascript version of these definitions in treeUtils.js
  //20030613 - Changed from private to package protected
  static final String ICON_TREE_BLANK           = "images/tree/blank.gif";
  static final String ICON_TREE_ANSBRANCH       = "images/tree/ansBranch.gif"; //oops... should be ANC (for ancestor). My bad. @todo: correct spelling one day!
  static final String ICON_TREE_LASTCHILDBRANCH = "images/tree/lastChildBranch.gif";
  static final String ICON_TREE_MIDCHILDBRANCH  = "images/tree/midChildBranch.gif";
  static final String ICON_TREE_PLUSNODE        = "images/tree/plusNode.gif";
  static final String ICON_TREE_MINUSNODE       = "images/tree/minusNode.gif";
  static final String ICON_TREE_PLUSROOTNODE    = "images/tree/plusRootNode.gif";
  static final String ICON_TREE_MINUSROOTNODE   = "images/tree/minusRootNode.gif";
  static final String ICON_TREE_NODE            = "images/tree/node.gif";
  
  //20030613AH
  static final String ICON_TREE_MIDCHILDPLUS    = "images/tree/midChildPlus.gif";
  static final String ICON_TREE_MIDCHILDMINUS   = "images/tree/midChildMinus.gif";
  static final String ICON_TREE_LASTCHILDPLUS  = "images/tree/lastChildPlus.gif";
  static final String ICON_TREE_LASTCHILDMINUS = "images/tree/lastChildMinus.gif";
  
  static final String ICON_TREE_EXPANDEDNODE    = "images/tree/expandedNode.gif";
  //...

  private static final String VISIBLE_ROW_STYLECLASS   = "visibleRow";
  private static final String HIDDEN_ROW_STYLECLASS    = "hiddenRow";
  //..

  //private static final String ICON_WIDTH = "16";


  public NavPageRenderer(RenderingContext rContext, Navgroup navgroup, ActionMapping actionMapping)
  {
    super(rContext);
    _navgroup = navgroup;
    //_actionMapping = actionMapping;
  }

  public void render() throws RenderingException
  { //20030310AH
    try
    {
      if(_navgroup == null) return;

      RenderingContext rContext = getRenderingContext();
      NavigationConfig config = getNavigationConfig(rContext);
      //Document will be there already as its the target for the pipeline
      includeJavaScript(IGlobals.JS_FRAME_METHODS); //20030408AH
      renderNavgroupId(rContext,_navgroup.getId());
      renderNavgroup(rContext, config, _navgroup, false);
      renderInitialHighlight(rContext); //20030328AH
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error performing nav page rendering",t);
    }
  }

  static NavigationConfig getNavigationConfig(RenderingContext rContext)
    throws GTClientException
  {
    try
    {
      ServletContext context = rContext.getServletContext();
      NavigationConfig navConfig = (NavigationConfig)context.getAttribute(NavigationConfigManager.NAVIGATION_CONFIG_KEY);
      if(navConfig == null) throw new NullPointerException("navConfig is null"); //20030310AH
      return navConfig;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to retrieve navigation configuration information",t);
    }
  }

  //...........................................................................................

  protected void renderNavgroupId(  RenderingContext rContext,
                                    String navgroupId)
    throws RenderingException
  {
    try
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append("navgroupId = '");
      buffer.append(navgroupId);
      buffer.append("';");
      addJavaScriptNode(null,buffer.toString());
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error in renderNavgroupId",t);
    }
  }

  protected void renderMessage(RenderingContext rContext, Message message)
    throws RenderingException
  {
    try
    {
      renderLabel(message.getId(),message.getLabel(),false);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering Message:" + message,t);
    }
  }

  protected void renderImage(RenderingContext rContext, Image image)
    throws RenderingException
  { //20021210AH [EXPERIMENTAL]
    try
    {
      Element imgNode = getElementById(image.getId(),false);
      if(imgNode != null)
      {
        String href = image.getSrc();
        if( (href != null) && (image.isRewrite()))
        {
          href = rContext.getUrlRewriter().rewriteURL(href,false);
          int nodeType = getNodeType(imgNode);
          switch(nodeType)
          {
            case INodeTypes.IMAGE:
                imgNode.setAttribute("src",href);
              break;

            default:
              Element newImage = _target.createElement("img");
              newImage.setAttribute("src",href);
              newImage.setAttribute("border","0");
              imgNode.appendChild(newImage);
              break;
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering Image:" + image,t);
    }
  }

  protected void renderNavlink(RenderingContext rContext, Navlink navlink)
    throws RenderingException
  {
    try
    {
      Element linkNode = getElementById(navlink.getId(),false);
      if(linkNode != null)
      { 
        //20050315AH - Check admin rights for link node, hiding if dont have
        boolean hideNode = false;
        if(navlink.isRequiresAdmin() 
        		|| navlink.isRequiresP2P()			//NSL20050424 Check for P2P
        		|| navlink.isRequiresUDDI())    // or UDDI requirement
        { 
          HttpSession session = rContext.getSession();
          IGTSession gtasSession = StaticWebUtils.getGridTalkSession(session);
          boolean isAdmin = gtasSession.isAdmin();
          boolean isNoP2P = gtasSession.isNoP2P();
          boolean isNoUDDI = gtasSession.isNoUDDI();
          if ((!isAdmin && navlink.isRequiresAdmin()) 
          		|| (isNoP2P && navlink.isRequiresP2P())    //NSL20060424 check if to hide P2P
          		|| (isNoUDDI && navlink.isRequiresUDDI())) // or UDDI functions
          {
            hideNode = true;
          }
        }
        
        
        //...
        if(hideNode)
        { //20050314AH
          //Special handling for FancyTabs (we remove them)
          Node parentNode = linkNode.getParentNode();
          if( nodeIsElement(parentNode) )
          {
            String cssClass = ((Element)parentNode).getAttribute("class");
            if("fancyTabDown".equals(cssClass) || "fancyTabUp".equals(cssClass) )
            {
              //get the td before this one, if its mid or firstdown
              
              removeFancyTab((Element)parentNode);
              
              //removeNode(parentNode,false);
            }
          }
          //Hide the node (ie: remove it)
          removeNode(linkNode, false);
        }
        else
        { //Normal rendering
          if("a".equalsIgnoreCase(linkNode.getNodeName()))
          { //20021201AH
            String url = rContext.getUrlRewriter().rewriteURL(navlink.getValue(),navlink.isClear()); //20030311AH
  
            String target = linkNode.getAttribute("target");
            if(StaticUtils.stringEmpty(target) && (url.indexOf("javascript:") == -1) )
            {
              String onclick = "loadFrame('gtas_body_frame','" + url + "');"; //20030312AH
              linkNode.setAttribute("onclick",onclick); //20030312AH
              linkNode.setAttribute("href", AnchorConversionRenderer.NOOP ); //20030313AH
            }
            else
            {
              linkNode.setAttribute("href",url ); //20030313AH
            }
          }
          //20021210AH - Preserve non-text nodes in link, but remove text, & entity nodes
          String linkText = rContext.getResourceLookup().getMessage(navlink.getLabel());
          replaceTextCarefully(linkNode, linkText);
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering Navlink:" + navlink,t);
    }
  }
  
  /**
   * Removes a fancy tab, making some assumptions about how their html is arranged.
   * If you change the way a fancyTab is rendered you may need to change this method
   * @param mainCell the main td for the tab
   * @throws RenderingException wrapping any caught exceptions
   */
  private void removeFancyTab(Element mainCell) throws RenderingException
  {
    try
    {
      //Search for the td before this one
      //Search for an img inside it
      //if the src is 'images/tabs/firstDown.gif' or 'images/tabs/midDownDown.gif'
      //etc...
      Element prevTd = getPrevious(mainCell,"td");
      Element prevImg = findElement(prevTd, "img", null, null);
      String prevSrc = prevImg.getAttribute("src");
      
      Element nextTd = getNext(mainCell,"td");
      Element nextImg = findElement(prevTd, "img", null, null);
      String nextSrc = nextImg.getAttribute("src");
      
      boolean isFirstTab = prevSrc.indexOf("first") != -1;
      boolean isLastTab = prevSrc.indexOf("first") != -1;
      boolean isOnlyTab = isFirstTab && isLastTab;
      
      if(isOnlyTab)
      {
        removeNode(prevTd,false);
        removeNode(mainCell,false);
        removeNode(nextTd,false);
        return;
      }
      
      if(isFirstTab)
      {
        removeNode(prevTd,false);
        removeNode(mainCell,false);
        nextImg.setAttribute("src",prevSrc);
        return;
      }
      
      if(isLastTab)
      {
        prevImg.setAttribute("src",nextSrc);
        removeNode(mainCell,false);
        removeNode(nextTd,false);
        return;
      }
      
      //else its a normal tab in the middle...
      removeNode(prevTd,false);
      removeNode(mainCell,false);
    }
    catch(Exception e)
    {
      throw new RenderingException("Error removing fancyTab",e);
    }
  }

  private void renderNavgroup(RenderingContext rContext,
                              NavigationConfig config,
                              Navgroup navgroup,
                              boolean isHeader)
    throws RenderingException
  { //20030305AH - Modified to recursively handle includes
    //Nb that the document parameter of included groups is ignored
    try
    {
      if(navgroup == null)
      {
        return;
      }
      Iterator i = navgroup.getChildren();
      while(i.hasNext())
      {
        IdentifiedBean child = (IdentifiedBean)i.next();
        //nb: must check for DynamicNavlink before Navlink because it is a subclass
        if(child instanceof Insert)
        { //20030617AH
          renderInsert(rContext, (Insert)child);
        }
        if(child instanceof DynamicNavlink)
        {
          renderDynamicNavlink(rContext,(DynamicNavlink)child);
        }
        if(child instanceof Navlink)
        {
          renderNavlink(rContext,(Navlink)child);
        }
        if(child instanceof NavTree)
        { //20030303AH
          renderNavTree(rContext, (NavTree)child);
        }
        else if(child instanceof Message)
        {
          renderMessage(rContext, (Message)child);
        }
        else if(child instanceof Include)
        { //20030305AH
          Navgroup include = config.getNavgroup(child.getId());
          renderNavgroup(rContext, config, include, false);
        }
        else if(child instanceof Image)
        { //20021210AH [EXPERIMENTAL]
          renderImage(rContext, (Image)child);
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering Navgroup:" + navgroup,t);
    }
  }

  protected void renderInsert(RenderingContext rContext, Insert insert)
    throws RenderingException
  { //20030617AH
    try
    {
      Element toNode = getElementById(insert.getId());
      if(toNode != null)
      {
        IDocumentManager docMgr = rContext.getDocumentManager();
        if (docMgr == null)
          throw new NullPointerException("docMgr is null");
        Document document = docMgr.getDocument(insert.getDocument(), false);
        if (document == null)
          throw new NullPointerException("document is null");
        String from = insert.getFrom();
        Node fromNode = (from == null) ? findElement(document,"body",null,null)
                                        : findElement(document,null,"id",from);
        removeAllChildren(toNode);
        if(fromNode != null)
        {
          NodeList children = fromNode.getChildNodes();
          int numChildren = children.getLength();
          for(int i=0; i < numChildren; i++)
          {
            Node copy = _target.importNode( children.item(i).cloneNode(true) , true);
            toNode.appendChild(copy);
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering Insert:" + insert,t);
    }
  }

  protected void renderDynamicNavlink(RenderingContext rContext, DynamicNavlink dynamicNavlink)
    throws RenderingException
  {
    try
    {
      renderNavlink(rContext, (Navlink)dynamicNavlink);

      String navRenderer = dynamicNavlink.getNavRenderer();
      if(dynamicNavlink.NAV_RENDERER_PROCESS_INSTANCE.equals(navRenderer))
      {
        ProcessInstanceNavRenderer pinr = new ProcessInstanceNavRenderer(getRenderingContext(), dynamicNavlink); //20030325AH
        pinr.render(_target);
      }
      else
      {
        throw new UnsupportedOperationException("navRenderer: "
                                                + navRenderer
                                                + " not supported");
      }

    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering DynamicNavlink:" + dynamicNavlink, t);
    }
  }

  /**
   * Render a navigation tree based on the supplied NavTree config object.
   * @param rContext
   * @param navTree
   * @throws RenderingException
   */
  protected void renderNavTree(RenderingContext rContext, NavTree navTree)
    throws RenderingException
  { //20030303AH
    try
    {
      // If the id for the insertion point is not found in target we will do nothing for this tree
      Element insertionPoint = getElementById(navTree.getId(),false);
      if(insertionPoint != null)
      {
        // Ensure the necessary javascript methods for the tree are included by the page
        includeJavaScript(IGlobals.JS_TREE_UTILS); //20030408AH

        // Replace whatever element is marked as the insertion point with a new table element
        Element tableNode = replaceNodeWithNewElement("table",insertionPoint,false);
        tableNode.setAttribute("border","0");
        tableNode.setAttribute("cellspacing","0");
        tableNode.setAttribute("cellpadding","0");

        // Get the rootNode of the navTree from the navTree config object
        NavTreeNode rootNode = navTree.getTree();
        String treeName = navTree.getId();

        // Create a StringBuffer in which we will build a javascript function that will
        // be included on the page. (This function sets up objects used to run the tree dhtml)
        StringBuffer setupBuffer = new StringBuffer(NAVTREE_SETUP_BUFFER_SIZE);

        // Append "function setupbob(){" (where bob is tree id)" to setupBuffer
        setupBuffer.append("function setup");
        setupBuffer.append(treeName);
        setupBuffer.append("()\n{\n");

        // Render the html for the rows of the tree into the supplied table element.
        // This also renders javascript code into the setupBuffer that will be used to initialise
        // a client side javascript in-memory representation of the tree structure that is used to
        // keep track of stuff for the expansion and collapsion dhtml stuff
        renderNavTreeNode(rContext, rootNode, tableNode, setupBuffer, navTree, true); //20030317AH

        // Render stuff to the setupBuffer to complete the function and return the rootNode object
        // for the in-memory tree.
        setupBuffer.append("return ");
        setupBuffer.append(navTree.getTree().getId());
        setupBuffer.append(";\n}\n");

        // Append javascript that will run as soon as the browser reads it that will call the
        // function we created resulting in the in-memory representation of the tree being stored
        // under a global (window) variable named by the id of the navTree.
        setupBuffer.append(treeName);
        setupBuffer.append(" = setup");
        setupBuffer.append(treeName);
        setupBuffer.append("();\n");

        // Find the head element of the target, and append a script node that contains the contents
        // of setupBuffer.
        Element headNode = this.getHeadNode();
        if(headNode == null)
        {
          throw new NullPointerException("Unable to find head node in target document");
        }
        addJavaScriptNode(headNode, setupBuffer.toString());
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering NavTree:" + navTree,t);
    }
  }

  /**
   * A recursive method to render a tree node row in a navtree. This method is called by either
   * renderNavtree() for the root node, or by itself for all descendant nodes. It will create
   * a tr element with appropriate content for this row of the tree and append it as a child of
   * tableNode passed in. It will also append javascript code into the provided setupBuffer.
   *
   * @param rContext
   * @param node The NavTreeNode to render a row for
   * @param tableNode - The table element into which the rows are inserted
   * @param setupBuffer - A StringBuffer to which  javascript for client side tree initialisation will be appended
   * @param navTree - The NavTree config object
   * @param visible - A flag indicating whether this row should be rendered as initially visible
   * @throws RenderingException
   *
   */
  protected void renderNavTreeNode( RenderingContext rContext,
                                    NavTreeNode node,
                                    Element tableNode,
                                    StringBuffer setupBuffer,
                                    NavTree navTree,
                                    boolean visible)
    throws RenderingException
  { //20030303AH, mod20030317AH
    try
    {
      // 20031113 DDJ: Support for DynamicNavTreeNode
      if(node instanceof DynamicNavTreeNode)
      {
        ((DynamicNavTreeNode)node).initDynamicChildNodes(rContext);
      }

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest()); //NSL20060424

      //Convienience variable to save us calling getChildCount() multiple times
      //boolean hasChildren = node.getChildCount() > 0; //20030317AH
      boolean hasChildren = hasVisibleChildren(node, gtasSession); //NSL20060425

      //The intial expansion status of the node. Nb that nodes with no children
      //will have this as true. Note also that the expansion status of a node relates not
      //to its own visibility, but to that of its children, and that a node can be expanded
      //but not visible as one of its ancestors is collapsed (this is why we pass in a visible
      //parameter to this method).
      boolean expanded = hasChildren ? node.isInitiallyExpanded() : true;//20030317AH

      //Conveinience reference to this nodes parent node
      NavTreeNode parent = node.getParent();

      //Render to setupBuffer javascript of the form: "var nodeId = new NavTreeNode('nodeId',parentId,expanded);\n"
      //where the values of "nodeId", "expanded" and "parentId" are substituted appropriately.
      //The end result is a javascript function in the setupBuffer that will added to the page
      //along with code to call it that will create an in-memory representation of the tree
      //in the browser to allow it to keep track of which rows to show hide when a node is
      //expanded or collapsed. The method renderNavTree() instantiated the setupBuffer object
      //and has kept a reference to allow it to insert the javascript into the target once the
      //tree elements have been rendered to the tableNode. (Likewise for the tableNode itself).
      String nodeId = node.getId();
      setupBuffer.append("var ");
      setupBuffer.append(nodeId);
      setupBuffer.append(" = new NavTreeNode('");
      setupBuffer.append(nodeId);
      setupBuffer.append("',");
      setupBuffer.append(parent == null ? "null" : parent.getId());
      setupBuffer.append(","); //20030317AH
      setupBuffer.append(expanded); //20030317AH
      
      //20030613AH - Support for customised node icons
      setupBuffer.append(",'");
      setupBuffer.append(node.getExpanded());
      setupBuffer.append("','");
      setupBuffer.append(node.getCollapsed());
      setupBuffer.append("'");
      //...
      
      setupBuffer.append(");\n");

      // Create the tr element for this node in the tree
      Element rowNode = _target.createElement("tr");
      rowNode.setAttribute("id",node.getId());
      rowNode.setAttribute("class",VISIBLE_ROW_STYLECLASS);

      //Create the td element that contains all the stuff for this row in the tree
      Element rowCell = _target.createElement("td");
      rowCell.setAttribute("nowrap","nowrap");
      rowCell.setAttribute("align","left");
      rowCell.setAttribute("valign","center");
      rowCell.setAttribute("border","0");
      rowCell.setAttribute("class","treeCell"); //20030313AH
      // Make the td a child of the tr
      rowNode.appendChild(rowCell);

      // Create the label for the tree node. This might be an anchor, or it might be plain
      //text:
      //String label = rContext.getResourceLookup().getMessage(node.getLabel()); // 20031113 DDJ: Do not i18n Dynamic nodes
      String label = node.getLabel();
      if(!(parent instanceof DynamicNavTreeNode))
      {
        label = rContext.getResourceLookup().getMessage(label); 
      }
      Text labelText = _target.createTextNode(label);
      
      if(node.getValue() != null)
      { // The value for the node is a url to which it links.
        Element anchor = _target.createElement("a");
        anchor.setAttribute("id",node.getId() + "_anchor"); //20030328AH
        anchor.setAttribute("class","inactiveAnchor"); //20030328AH
        String url = rContext.getUrlRewriter().rewriteURL(node.getValue(),node.isClear());
        //@todo: dont hardcode method names!
        String onclick = "loadFrame('gtas_body_frame','" + url + "');"; //20030312AH
        anchor.setAttribute("onclick",onclick); //20030312AH
        anchor.setAttribute("href",AnchorConversionRenderer.NOOP);
        anchor.appendChild(labelText);
        rowCell.appendChild(anchor);
      }
      else
      {
        // Nodes with simple text labels are a wee bit less complex ;-)
        rowCell.appendChild(labelText);
      }
      //Create the 'main' icon - this is the icon that is closest to the node text
      String mainIconUrl = expanded ? node.getExpanded() : node.getCollapsed(); //20030624AH
      /*if(mainIconUrl == null)
      { //20030624AH - Default handling if not overridden by navigation config
        if(parent == null)
        {
          mainIconUrl = expanded ? ICON_TREE_MINUSROOTNODE : ICON_TREE_PLUSROOTNODE;
        }
        else
        {
          if(hasChildren)
          {
            mainIconUrl = expanded ? ICON_TREE_EXPANDEDNODE : ICON_TREE_NODE;
          }
          else
          {
            mainIconUrl = ICON_TREE_NODE;
          }
        }
      }*/
      Element mainIcon = createImageNode(mainIconUrl , node.getId() + "_icon");
      //Set the icons dhtml properties. Parent nodes need to fire js to expand or collapse
      //while leaf nodes are just there to look pretty
      mainIcon.setAttribute("style","vertical-align: text-top; margin-right: 3px;");
      //And add the mainIcon image for this tree node to the row (to the left of the text or link
      //we already inserted in the DOM)
      insertFirstChild(rowCell, mainIcon);
      //...

      // Now we must look back up the tree structure to work out how to render the icons to the
      // left of the main icon. Which images we choose depend on the relationships between the
      // nodes in the tree structure. We start however with the current node.

      //The consideration node, is the node in the tree that we are looking at to determine which
      //image we need to insert on the left.
      NavTreeNode consideration = node;
      while( consideration.getParent() != null )
      {
        // Based on the properties of the consideration node we choose an appropriate image for
        // the 'column' (nb: we dont actaually use table columns for these columns as there
        // are sizing problems to consider. Instead we are just putting in equally sized images
        // into a single td for each row of the tree).
        String icon = null;
        String iconElementId = null;
        if(node == consideration)
        { //XXXIf we are 'considering' the current node (that this method is rendering a row for)
          //then we need to render an image that links up visually on the right with our mainIcon
          //image. This image will also link up visually with the one above it. Whether it also
          //links up with the one below, depends on whether this node is the last child of its
          //parent or not. If it is last, then the image doesnt have a line going out the
          //bottom as there are no more children on this branch.
        	
          //NSL20060425 check for last visible (not programmed to hide) child instead of physical last child
        	boolean lastVisibleChild = isLastVisibleChild(node, gtasSession); 
        	if(hasChildren)
          { 
            //icon = node.isLastChild()?  ICON_TREE_LASTCHILDPLUS : ICON_TREE_MIDCHILDPLUS;
        		icon = lastVisibleChild?  ICON_TREE_LASTCHILDPLUS : ICON_TREE_MIDCHILDPLUS;
          }
          else
          {
            //icon = node.isLastChild() ? ICON_TREE_LASTCHILDBRANCH : ICON_TREE_MIDCHILDBRANCH;
            icon = lastVisibleChild ? ICON_TREE_LASTCHILDBRANCH : ICON_TREE_MIDCHILDBRANCH;
          }
          iconElementId = node.getId() + "_branch";
        }
        else
        { //Once we are considering nodes above the node whose row is being rendered, there are
          //two possibilies for the image. If the node being considered is the last child of
          //its parent then we do not need a vertical line to link up with further children in
          //lower rows of the table so we will insert a blank spacer image. If however its parent
          //has additional children we insert a vertical line that links up with images above and
          //below - but not left or right.

          //NSL20060425 check for last visible (not programmed to hide) child instead of physical last child
        	boolean lastVisibleChild = isLastVisibleChild(consideration, gtasSession); 
        	//if(consideration.isLastChild() )
        	if(lastVisibleChild)
          {
            icon = ICON_TREE_BLANK;
          }
          else
          {
            icon = ICON_TREE_ANSBRANCH;
          }
        }
        //Create the image element using the appropriate image url.
        Element iconElement = createImageNode(icon, iconElementId);
        if( (node == consideration) && hasChildren)
        {
          String onclick = navTree.getId() + ".getNode('" + node.getId() + "').toggleExpanded();";
          iconElement.setAttribute("onclick",onclick);
          //The style used for clickable nodes is slightly different - it specifies a different
          //cursor to give the user an additional visual cue that the node can be clicked
          iconElement.setAttribute("style","cursor: crosshair; vertical-align: text-top;");
        }
        else
        { 
          iconElement.setAttribute("style","margin: 0px; padding: 0px; vertical-align: text-top;"); //20030313AH
        }
        insertFirstChild(rowCell, iconElement); // Insert to the left of stuff already in the cell
        //Now we move the consideration pointer one level up the tree in preperation for rendering
        //the image to the left of the one we just created. (If the consideration node is now the
        //root node then the iteration will end already)
        consideration = consideration.getParent();
      }
      // The row is complete. Append it to the table with appropriate style class.
      String styleClass = visible ? VISIBLE_ROW_STYLECLASS : HIDDEN_ROW_STYLECLASS;
      rowNode.setAttribute("class",styleClass);
      tableNode.appendChild(rowNode);

      // Now we recursively call this method for each child of the node that rendered. This
      // will result in them being rendered in the appropriate rows of the table so that our
      // expandy collapsy stuff works nice.

      Iterator iterator = node.getChildren();
      if(iterator != null)
      {
        while(iterator.hasNext())
        {
          NavTreeNode child = (NavTreeNode)iterator.next();
          //NSL20060424 Check for P2P and UDDI requirement before rendering
          if ((!child.isRequiresP2P() || !gtasSession.isNoP2P()) && (!child.isRequiresUDDI() || !gtasSession.isNoUDDI()))
          {
          	//For the childnode to be rendered as initially visible, it needs all its ancestors to
          	//be expanded. Note that as a result of this logic it is quite possible for the child
          	//to be expanded yet not visible, as one of its ancestors is collapsed. This is why
          	//the visible property is passed seperately instead of merely being the same as the
          	//expanded property.
          	boolean childVisible = expanded && visible;
          	//Make recursive call to this method to render the child and all its children before
          	//we render the next child of this node.
          	renderNavTreeNode(rContext, child, tableNode, setupBuffer, navTree, childVisible);
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering NavTreeNode:" + node,t);
    }
  }

  /**
   * Check if the node is the last visible child of its parent. By 'visible', it means physically attached to the parent when displayed.
   * A node may not be visible if it requires P2P or UDDI to be enabled but which is not.
   * @param node The node to check.
   * @param gtasSession
   * @return if all of its following siblings are not visible, then it is the last visible child.
   */
  protected boolean isLastVisibleChild(NavTreeNode node, IGTSession gtasSession)
  {
  	if ((gtasSession.isNoP2P() || gtasSession.isNoUDDI()) && !node.isLastChild() && node.getParent()!=null)
  	{
  		Iterator siblingsIter = node.getParent().getChildren();
  		boolean foundNode = false; 
  		boolean anySiblingVisible = false;
  		while (siblingsIter.hasNext() && !anySiblingVisible)
  		{
  			NavTreeNode sibling = (NavTreeNode)siblingsIter.next();
  			if (foundNode)
  			{
  				anySiblingVisible = (!sibling.isRequiresP2P() || !gtasSession.isNoP2P()) && (!sibling.isRequiresUDDI() || !gtasSession.isNoUDDI());
  			}
  			else if (node.getId().equals(sibling.getId()))
  			{
  				foundNode = true;
  			}
  		}
  		return !anySiblingVisible;
  	}
  	else
  	{
  		return node.isLastChild();
  	}
  }
  
  /**
   * Check if the node has any visible children node. By 'visible', it means physically attached to the parent when displayed.
   * A node may not be visible if it requires P2P or UDDI to be enabled but which is not.
   * @param node The node to check
   * @param gtasSession
   * @return <b>true</b> if at least one of its child node is visible, otherwise <b>false</b>. 
   */
  protected boolean hasVisibleChildren(NavTreeNode node, IGTSession gtasSession)
  {
  	if (node.getChildCount() == 0)
  	{
  		return false;
  	}
  	if (!gtasSession.isNoP2P() && !gtasSession.isNoUDDI())
  	{
  		return node.getChildCount() > 0;
  	}
  	Iterator childrenIter = node.getChildren();
  	boolean anyChildVisible = false;
  	while (childrenIter.hasNext() && !anyChildVisible)
  	{
  		NavTreeNode child = (NavTreeNode)childrenIter.next();
  		anyChildVisible = (!child.isRequiresP2P() || !gtasSession.isNoP2P()) && (!child.isRequiresUDDI() || !gtasSession.isNoUDDI());
  	}
  	return anyChildVisible;
  }
  
  /**
   * Convienience method to create an img element with specified src and id properties and
   * a border of 0.
   * @param src The url of the image
   * @param id The unique id of the image node
   * @return img An img element node
   * @throws RenderingException
   */
  protected Element createImageNode(String src, String id)
    throws RenderingException
  { //20030304AH
    try
    {
      //Internal sanity assertions:
      if(src == null) throw new NullPointerException("src is null"); //20030317AH
      //...
      Element img = _target.createElement("img");
      img.setAttribute("border","0");
      img.setAttribute("src",src);
      if(id != null)
      {
        img.setAttribute("id",id);
      }
      return img;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating image node with src=" + src + " id=" + id,t);
    }
  }

  protected void renderInitialHighlight(RenderingContext rContext) throws RenderingException
  { //20030328AH
    try
    {
      String highlightId = rContext.getRequest().getParameter("highlightId");
      if(StaticUtils.stringNotEmpty(highlightId))
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append("setHighlight('");
        buffer.append(highlightId);
        buffer.append("');");
        appendOnloadEventMethod(buffer.toString());
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering initial highlight invocation script",t);
    }
  }
}
/*
	*	File:		ecms_config.js
	*	Name:		Expandable/Collapsible Menu System
	*	Date:		2001-10-06

	*	Author:		Ben Boyle
	*	Email:		bboyle@inspire.server101.com

	*	Platform:	all javascript browsers

	Configuration file for dynamic expandable/collapsible menu.
	Remove the // from comments and adjust properties to suit.

	see instructions online:
	http://inspire.server101.com/scripts/ecms/


	Functions to control menu from HTML:
		ECMS_expand_all()		(expand entire menu)
		ECMS_collapse_all()		(collapse entire menu)
		ECMS_expand('name')		(expand named menu folder)
		ECMS_collapse('name')	(collapse named menu folder)
	note: to expand/collapse a named subfolder, its parent must be expanded

	CSS display configuration classes (class names can be customised):
		.bullet		(a menu link that does not contain child items)
		.collapsed	(a menu parent that is closed)
		.expanded	(a menu parent that is open)

	CSS platform dependant styles:
		IE 4/5, N6	(DIV.stylename)
		Netscape 4x	(LAYER.stylename)
		default		(LI.stylename)

	CSS examples:
		DIV.bullet { font-weight: normal; }
		DIV.collapsed { font-weight: bold; }
		DIV.expanded { font-weight: bold; }
		LAYER.bullet { font-weight: normal; }
		LAYER.collapsed { font-weight: bold; }
		LAYER.expanded { font-weight: bold; }
		LI.bullet { font-weight: normal; list-style-type: disc; list-style-image: url(bullet.gif); }
		LI.expanded { font-weight: bold; list-style-type: circle; list-style-image: url(expanded.gif); margin-bottom: 0px; margin-left: 15px; margin-top: 0px; }

*/

/*
	menu classes
	- bullet (menu item)
	- collapsed (collapsed menu item which expands when clicked)
	- expanded (expanded menu item which collapses with clicked)
*/
ECMS_class_bullet = 'bullet';
ECMS_class_collapsed = 'collapsed';
ECMS_class_expanded = 'expanded';

/*
	menu images (not used by default system)
	- bullet (menu item)
	- collapsed (collapsed menu item which expands when clicked)
	- expanded (expanded menu item which collapses with clicked)
*/
ECMS_image_bullet.src = '../javascript/bullet.gif';
ECMS_image_collapsed.src = '../javascript/collapsed.gif';
ECMS_image_expanded.src = '../javascript/expanded.gif';

/*
	menu images ALT text (not used by default system)
	- bullet (menu item)
	- collapsed (collapsed menu item which expands when clicked)
	- expanded (expanded menu item which collapses with clicked)
*/
ECMS_image_bullet.alt = '';
ECMS_image_collapsed.alt = 'menu (show)';
ECMS_image_expanded.alt = 'menu (hide)';

/*
	indent menu items (pixel measurement from left margin)
*/
ECMS_menu_indent = 15;

/*
	height of fully expanded menu for netscape 4 (pixel measurement)
	this is to ensure enough space reserved for the menu
*/
//ECMS_menu_height = 125;

/*
	display link titles in status bar
	- if no title available, will use the name parameter
*/
ECMS_status_display = true;

/*
	repeat links on image/text
	- adds expand link on menus without their own links
	- adds link on bullet points
*/
ECMS_repeat_links = true;

/*
	collapse all other open menu branches when expanding a new branch
	values:	'menu' (collapse only within this menu)
			'all' (collapse all other menus as well)
*/
ECMS_collapse_on_expand = true;

/*
	automatically expand when the mouse is over a menu element
	delay time is in milliseconds (1000ms is 1 second)
	only works for menu folders which have a url specified
*/
ECMS_auto_expand = false;
ECMS_auto_expand_delay = 750;


/*
BUILD THE MENU
ECMS_add_item(level, name, url, target, expanded, title);
	- level:		level of the item (0 is a top level menu choice, 1 under that, 2 under that)
	- name:			text displayed for the item (HTML markup allowed)
	- url:			link for this item
	- target:		target frame for link
	- expanded:		true/false (initial expanded?  false is default)
	- title:		title for link (often appears as a tooltip on the link)
*/

// home (example menu)
ECMS_add_item(0, 'Profile');
ECMS_add_item(1, 'Name ','javascript:parent.frames[0].setUpXPath(\'Profile/Name\');','');
ECMS_add_item(2, 'Address1 (<a href=javascript:parent.frames[0].setUpXPath(\'Profile/Name/Address1@Residential\');>Residential</a>,<a href=javascript:parent.frames[0].setUpXPath(\'Profile/Name/Address1@Owned\');>Owned)</a>','javascript:parent.frames[0].setUpXPath(\'Profile/Name/Address1\');','');
ECMS_add_item(2, 'Address2','javascript:parent.frames[0].setUpXPath(\'Profile/Name/Address2\');','');

/*		


// help (example menu)
ECMS_add_item(0, 'ECMS instructions', 'load.html');
ECMS_add_item(1, 'Loading the javascript files', 'load.html');
	ECMS_add_item(2, 'Scripts explained', 'load.html#ecms');
	ECMS_add_item(2, 'Loading the core system', 'load.html#core');
	ECMS_add_item(2, 'Loading the platform libraries', 'load.html#platform');
	ECMS_add_item(2, 'Loading the configuration file(s)', 'load.html#config');
ECMS_add_item(1, 'Creating a menu', 'create.html');
	ECMS_add_item(2, 'Creating a new menu', 'create.html#new');
	ECMS_add_item(2, 'Defining an item', 'create.html#define');
	ECMS_add_item(2, 'Adding items to folders', 'create.html#folderitem');
	ECMS_add_item(2, 'An example menu', 'create.html#example');
ECMS_add_item(1, 'Customising the menu', 'customise.html');
ECMS_add_item(1, 'Using multiple menus', 'multiple.html');
ECMS_add_item(1, 'Troubleshooting', 'trouble.html');

*/
ECMS_end_menu();


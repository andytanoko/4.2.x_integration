/*
	*	File:		ecms_system.js
	*	Name:		Expandable/Collapsible Menu System
	*	Date:		2001-10-06

	*	Author:		Ben Boyle
	*	Email:		bboyle@inspire.server101.com

	*	Platform:	all javascript browsers
	*	Namespace:	all variables and functions prefixed with "ECMS_"

	System functions (cross-platform) shared by menu system.
	Dynamically loads correct platform library (IE, Netscape, default) to handle menu.
	All configuration handled through ECMS_config.js and CSS classes.

*/


// LOADER - loads browser specific version
if (document.getElementById) {
	// standard - Netscape6, IE 4/5
	document.writeln('<script language="Javascript" type="text\/javascript" src="../javascript\/ecms_system_id.js"><\/script>');
} else if (document.layers) {
	// layers (Netscape 4, 3)
	document.writeln('<script language="Javascript" type="text\/javascript" src="../javascript\/ecms_system_layer.js"><\/script>');
} else {
	// default - uses UL/LI display
	document.writeln('<script language="Javascript" type="text\/javascript" src="../javascript\/ecms_system_default.js"><\/script>');
}


/*
 ECMS_config.js variables
 - The following variables can all be user-defined in ECMS_config.js
*/

// expand/collapse images
var ECMS_image_bullet = new Image();
var ECMS_image_collapsed = new Image();
var ECMS_image_expanded = new Image();

// CSS classes
var ECMS_class_bullet = 'bullet';
var ECMS_class_collapsed = 'collapsed';
var ECMS_class_expanded = 'expanded';

// menu indent level
var ECMS_menu_indent = 0;

// menu height level (used by netscape to ensure expandable space)
var ECMS_menu_height = 0;

// display title (or name) in status bar
var ECMS_status_display = false;

// repeat links with images/text
var ECMS_repeat_links = false;

// automatically expand (onfocus, onmouseover)
var ECMS_auto_expand = false;
var ECMS_auto_expand_delay = 0;

// collapse other menu trees when expanding
var ECMS_collapse_on_expand = '';

/*
 end ECMS_config.js variables
*/


// menu
var ECMS_menu_tree = new Array();
var ECMS_menu_tree_index = 0;
// caches
var ECMS_image_cache = new Array();
var ECMS_class_cache = new Array();
// auto expand timer
var	ECMS_auto_expand_timer;


// new menu_tree object
function ECMS_menu_node(id, name, url, target, expanded, title, image) {
	this.id			= id.toString();
	this.expanded	= (expanded ? true : false);
	this.name		= name;
	this.url		= (url ? url : null);
	this.title		= (title ? title : name);
	this.target		= (target ? target : null);
	this.tree		= ECMS_menu_tree_index;
	this.expandedi	= (ECMS_image_expanded ? ECMS_find_image(ECMS_image_expanded) : null);
	this.collapsedi	= (ECMS_image_collapsed ? ECMS_find_image(ECMS_image_collapsed) : null);
	this.bulleti	= (ECMS_image_bullet ? ECMS_find_image(ECMS_image_bullet) : null);
	this.expandedc	= (ECMS_class_expanded ? ECMS_find_class(ECMS_class_expanded) : null);
	this.collapsedc	= (ECMS_class_collapsed ? ECMS_find_class(ECMS_class_collapsed) : null);
	this.bulletc	= (ECMS_class_bullet ? ECMS_find_class(ECMS_class_bullet) : null);
}


// adds a node to the end of the menu tree
function ECMS_add_item(level, name, url, target, expanded, title, node) {
	if (!node) {
		if (!ECMS_menu_tree[ECMS_menu_tree_index]) {
			// first item
			ECMS_menu_tree[ECMS_menu_tree_index] = new ECMS_menu_node(0, name, url, target, expanded, title);
			// menu settings
			ECMS_menu_tree[ECMS_menu_tree_index].menu_height = ECMS_menu_height;
			ECMS_menu_tree[ECMS_menu_tree_index].status_display = ECMS_status_display;
			ECMS_menu_tree[ECMS_menu_tree_index].repeat_links = ECMS_repeat_links;
			ECMS_menu_tree[ECMS_menu_tree_index].auto_expand = ECMS_auto_expand;
			ECMS_menu_tree[ECMS_menu_tree_index].auto_expand_delay = ECMS_auto_expand_delay;
			ECMS_menu_tree[ECMS_menu_tree_index].collapse_on_expand = ECMS_collapse_on_expand;
			return;
		} else {
	 		node = ECMS_menu_tree[ECMS_menu_tree_index];
		}
	}

	// end of level
	while (node.next) {
		node = node.next;
	}

	if (level == 0) {
		// add to this level
		var id = node.id.lastIndexOf('-');
		if (id != -1) {
			var inc = node.id.substring(id + 1);
			inc++;
			id = node.id.substring(0, id) + '-' + inc;
		} else {
			id = parseInt(node.id) + 1;
		}
		node.next = new ECMS_menu_node(id, name, url, target, expanded, title);
	} else if (node.child) {
		// recurse on next level
		ECMS_add_item(level - 1, name, url, target, expanded, title, node.child);
	} else {
		// new child
		node.child = new ECMS_menu_node(node.id + '-0', name, url, target, expanded, title);
	}
}


// include a class
function ECMS_include_class(i) {
	var cl = ECMS_class_cache[i];
	return (cl ? ' class="' + cl + '"' : '');
}

// include a image
function ECMS_include_image(node, i) {
	var image = ECMS_image_cache[i];
	if (image) {
		var alt = (node.child ? node.name + ' ' + image.alt : image.alt);
		return '<img src="' + image.src + '" ' + (image.width ? 'width="' + image.width + ' ' : '') + (image.height ? 'height="' + image.height + ' ' : '') + 'border="0" alt="' + alt + '" ID="ECMS_' + node.tree + '|' + node.id + '_IMG" />';
	}
	return '';
}
// include a bullet image
function ECMS_include_bullet_image(node) {
	var image = ECMS_include_image(node, node.bulleti);
	// link the image
	if (node.url && ECMS_menu_tree[node.tree].repeat_links) {
		image = ECMS_include_node_link(node, image);
	}
	return image;
}

// include control image
function ECMS_include_control_image(node, image) {
	return ECMS_include_control_link(node, ECMS_include_image(node, image));
}

// include control link
function ECMS_include_control_link(node, text) {
	return '<a class="side"  style="text-decoration:none" href="javascript:ECMS_control_click(' + node.tree + ', \'' + node.id + '\');" title="' + node.title + '" target="_self"' + ECMS_include_behaviours(node) + '>' + text + '</a>';
}

// include behaviours
function ECMS_include_behaviours(node, text) {
	var f = ''; // focus handler
	var f = ''; // blur handler
	var menu = ECMS_menu_tree[node.tree];

	// status
	if (menu.status_display) {
		f = 'window.status = \'' + node.title + '\';';
		b = 'window.status = \'\';';
	}

	// auto expand
	if (menu.auto_expand && node.child) {
		f += 'ECMS_set_auto_expand(' + node.tree + ', \'' + node.id + '\');';
		b += 'ECMS_set_auto_expand();';
	}
	var t = 'return true;';

	return (f ? ' onfocus="' + f + t +'" onblur="' + b + t +'" onmouseover="' + f + t +'" onmouseout="' + b + t +'"' : '');
}

// include a node link
function ECMS_include_node_link(node, display) {
	var text = (display ? display : node.name);
	if (node.url) {
		text = '<a class="side"  style="text-decoration:none" href="' + node.url + '" title="' + node.title + '"' + (node.target ? ' target="' + node.target + '"' : '') + ECMS_include_behaviours(node) + '>' + text + '</a>';
	} else if (node.child && ECMS_menu_tree[node.tree].repeat_links) {
		text = ECMS_include_control_link(node, node.name);
	}

	return text;
}

// include node
function ECMS_include_node(node, cl, i) {
	var	text = '<table border="0" cellpadding="0" cellspacing="0"><tr>';
	text += '<td valign="top">' + i + '&nbsp;</td>';
	text += '<td valign="top"' + ECMS_include_class(cl) + ' id="ECMS_' + node.tree + '|' + node.id + '_CL">' + ECMS_include_node_link(node) + '</td>';
	text += '</tr></table>';

	return text;
}

// find a node with a given id
function ECMS_find_node(tree, id) {
	var node = ECMS_menu_tree[tree];
	var level = id.toString().split('-');

	for (var i = 0; i < level.length; i++) {
		for (var j = level[i]; j > 0; j--) {
			node = node.next;
		}
		if (i + 1 < level.length) {
			node = node.child;
		}
	}

	return node;
}


// find a node with a given name
function ECMS_find_node_name(tree, name) {
	if (!tree) {
		tree = 0;
	}
	return ECMS_find_node_name_in_tree(ECMS_menu_tree[tree], name);
}
function ECMS_find_node_name_in_tree(node, name) {

	// found it (only if it is a parent node)
	if (node.child && node.name == name) {
		return node;
	}
	
	// recurse: check expanded children
	if (node.child && node.expanded) {
		var check = ECMS_find_node_name_in_tree(node.child, name);
		if (check) {
			return check;
		}
	}

	// recurse: check next nodes
	if (node.next) {
		var check = ECMS_find_node_name_in_tree(node.next, name);
		if (check) {
			return check;
		}
	}

	return null;
}


// control click to expand/collapse menu
function ECMS_control_click(tree, node, image) {
	node = ECMS_find_node(tree, node);
	ECMS_control_node(node, !node.expanded, image);
}


// expand a node based on name
function ECMS_expand(name, tree) {
	ECMS_control_node(ECMS_find_node_name(tree, name), 1);
}


// expand a node based on id
function ECMS_expand_id(tree, id) {
	ECMS_control_node(ECMS_find_node(tree, id), 1);
}


// collapse a node based on its name
function ECMS_collapse(name, tree) {
	ECMS_control_node(ECMS_find_node_name(tree, name), 0);
}


// expand all folders
function ECMS_expand_all(tree) {
	var collapse_on_expand = ECMS_menu_tree[tree].collapse_on_expand;
	// turn of auto collapsing
	ECMS_menu_tree[tree].collapse_on_expand = false;
	// expand all
	ECMS_expand_tree(ECMS_menu_tree[tree]);
	// restore auto collapsing setting
	ECMS_menu_tree[tree].collapse_on_expand = collapse_on_expand;
}


// expand a menu tree
function ECMS_expand_tree(node) {
	if (node.child) {
		ECMS_expand_tree(node.child);
		ECMS_control_node(node, 1);
	}

	// recurse
	if (node.next) {
		ECMS_expand_tree(node.next);
	}
}


// collapse all folders (exclude node path)
function ECMS_collapse_all(tree, path) {
	ECMS_collapse_tree(ECMS_menu_tree[tree], path);
}


// collapse a menu tree (exclude those in a given path)
function ECMS_collapse_tree(node, path) {
	if (node.child) {
		if (!path || path.id.indexOf(node.id) != 0) {
			ECMS_control_node(node, 0);
		}
		ECMS_collapse_tree(node.child, path);
	}
	// recurse
	if (node.next) {
		ECMS_collapse_tree(node.next, path);
	}
}


// control click to expand/collapse menu
function ECMS_control_node(node, expand) {
	if (expand) {
		if (!node.expanded) {
			// collapse other menus?
			var collapse = ECMS_menu_tree[node.tree].collapse_on_expand;
			if (collapse) {
				// collapse other trees
				if (collapse == 'all') {
					for (var i = 0; i < ECMS_menu_tree.length; i++) {
						if (i != node.tree) {
							ECMS_collapse_all(i);
						}
					}
				}
				// collapse other items within this tree
				ECMS_collapse_all(node.tree, node);
			}
			node.expanded = true;
			ECMS_PF_expand(node);
		}
	} else {
		if (node.expanded) {
			node.expanded = false;
			ECMS_PF_collapse(node);
		}
	}
}


// auto expand
function ECMS_set_auto_expand(tree, node) {
	if (!node && ECMS_auto_expand_timer) {
		clearTimeout(ECMS_auto_expand_timer);
	} else if (ECMS_menu_tree[tree].auto_expand) {
		ECMS_auto_expand_timer = setTimeout('ECMS_expand_id(' + tree + ', \'' + node + '\');', ECMS_menu_tree[tree].auto_expand_delay);
	}
}


// find image - caches images
function ECMS_find_image(image) {
	var i = 0;
	while (i < ECMS_image_cache.length) {
		if (ECMS_image_cache[i].src == image.src) {
			return i;
		}
		i++;
	}
	// cache new image - create copy
	ECMS_image_cache[i] = new Image();
	ECMS_image_cache[i].src = image.src;
	ECMS_image_cache[i].alt = image.alt;
	return i;
}


// find class - caches classes
function ECMS_find_class(name) {
	var i = 0;
	while (i < ECMS_class_cache.length) {
		if (ECMS_class_cache[i] == name) {
			return i;
		}
		i++;
	}
	// cache new class name
	ECMS_class_cache[i] = name;
	return i;
}


// end menu - trigger to create menu on page
function ECMS_end_menu() {
	if (ECMS_menu_tree[ECMS_menu_tree_index]) {
		// root node exists, continue
		ECMS_PF_create_menu(ECMS_menu_tree[ECMS_menu_tree_index]);
		ECMS_menu_tree_index++;
	}
}

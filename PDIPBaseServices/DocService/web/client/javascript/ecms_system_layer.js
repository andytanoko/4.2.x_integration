/*
	*	File:		ecms_system_layer.js
	*	Name:		Expandable/Collapsible Menu System
	*	Date:		2001-10-06

	*	Author:		Ben Boyle
	*	Email:		bboyle@inspire.server101.com

	*	Platform:	Netscape Navigator 4.x (N6 = system_id.js)

	Nestcape menu handling through <layer>s.

*/


// netscape menu layout handling
onLoad = ECMS_PF_layout_menus;
onResize = ECMS_PF_layout_menus;

// create the menu
function ECMS_PF_create_menu(root) {
	document.write('<ilayer height="' + ECMS_menu_tree[root.tree].menu_height + '" id="ECMS_root' + root.tree + '" visibility="show">\n');
	document.write(ECMS_PF_write_menu(root, 0));
	document.write('</ilayer>\n');
	// initial position (onLoad handler would be better, but might be overridden in HTML or other scripts)
	setTimeout('ECMS_PF_control_tree(ECMS_menu_tree[' + root.tree + '], true, document[\'ECMS_root' + root.tree + '\'].pageY);', 750);
}

// write a tree
function ECMS_PF_write_menu(node, left) {
	var text = '';
	if (node.child) {
		// collapsed layer
		text += '<layer id="ECMS_' + node.tree + '|' + node.id + '_C" left="' + left + '" visibility="show">';
		text += ECMS_include_node(node, node.collapsedc, ECMS_include_control_image(node, node.collapsedi));
		text += '</layer>\n';
		// expanded layer
		text += '<layer id="ECMS_' + node.tree + '|' + node.id + '_E" left="' + left + '" visibility="show">';
		text += ECMS_include_node(node, node.expandedc, ECMS_include_control_image(node, node.expandedi));
		text += '</layer>\n';
		// children
		text += ECMS_PF_write_menu(node.child, left + ECMS_menu_indent);
	} else {
		text += '<layer id="ECMS_' + node.tree + '|' + node.id + '" left="' + left + '" visibility="show">';
		text += ECMS_include_node(node, node.bulletc, ECMS_include_bullet_image(node));
		text += '</layer>\n';
	}

	// estimate menu height if none specified
	if (!ECMS_menu_height) {
		// guess based on 1 line/item (no text wrapping, small images)
		text += '<br>\n';
	}

	if (node.next) {
		text += ECMS_PF_write_menu(node.next, left);
	}
	return text;
}


// expand a node
function ECMS_PF_expand(node) {
	ECMS_PF_control_tree(ECMS_menu_tree[node.tree], true, document['ECMS_root' + node.tree].pageY);
}


// collapse a node
function ECMS_PF_collapse(node) {
	ECMS_PF_control_tree(ECMS_menu_tree[node.tree], true, document['ECMS_root' + node.tree].pageY);
}


// control tree (toggles layer visibility and positioning)
function ECMS_PF_control_tree(node, visible, top) {
	var layer = ECMS_PF_get_layer(node);
	if (visible) {
		layer.moveBy(0, top - layer.pageY);
		top += layer.document.height;
		layer.visibility = 'show';
	} else {
		layer.visibility = 'hide';
	}

	if (node.child) {
		if (visible && node.expanded) {
			top = ECMS_PF_control_tree(node.child, true, top);
		} else {
			top = ECMS_PF_control_tree(node.child, false, top);
		}
	}
	if (node.next) {
		top = ECMS_PF_control_tree(node.next, visible, top);
	}
	return top;
}


// get layer for a node
function ECMS_PF_get_layer(node) {
	if (node.child) {
		if (node.expanded) {
			layer = document['ECMS_root' + node.tree].layers['ECMS_' + node.tree + '|' + node.id + '_E'];
			// hide the collapsed layer
			document['ECMS_root' + node.tree].layers['ECMS_' + node.tree + '|' + node.id + '_C'].visibility = 'hide';
		} else {
			layer = document['ECMS_root' + node.tree].layers['ECMS_' + node.tree + '|' + node.id + '_C'];
			// hide the expanded layer
			document['ECMS_root' + node.tree].layers['ECMS_' + node.tree + '|' + node.id + '_E'].visibility = 'hide';
		}
	} else {
		layer = document['ECMS_root' + node.tree].layers['ECMS_' + node.tree + '|' + node.id];
	}
	return layer;
}


// layout all menus
function ECMS_PF_layout_menus() {
	// render menus again
	for (var i = 0; i < ECMS_menu_tree.length; i++) {
		ECMS_PF_control_tree(ECMS_menu_tree[i], true, document['ECMS_root' + i].pageY);
	}
}


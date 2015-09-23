/*
	*	File:		ecms_system_id.js
	*	Name:		Expandable/Collapsible Menu System
	*	Date:		2001-10-06

	*	Author:		Ben Boyle
	*	Email:		bboyle@inspire.server101.com

	*	Platform:	document.getElementById (Ie4/5, Netscape 6)

	Menu handling.

*/


// create the menu
function ECMS_PF_create_menu(root) {
	document.write(ECMS_PF_write_menu(root));
}


// write a tree
function ECMS_PF_write_menu(node) {
	var text = '';
	if (node.child) {
		text += ECMS_include_node(node, (node.expanded ? node.expandedc : node.collapsedc), ECMS_PF_include_control_image(node));
		text += '\n<div id="ECMS_' + node.tree + '|' + node.id + '_CH" style="display: ' + (node.expanded ? 'block' : 'none') + '; padding-left: ' + ECMS_menu_indent + 'px;">\n';
		text += ECMS_PF_write_menu(node.child);
		text += '</div>\n';
	} else {
		text += ECMS_include_node(node, node.bulletc, ECMS_include_bullet_image(node));
	}

	if (node.next) {
		text += ECMS_PF_write_menu(node.next);
	}
	return text;
}


// expand a node
function ECMS_PF_expand(node) {
	ECMS_PF_image_swap(node, 1);
	document.getElementById('ECMS_' + node.tree + '|' + node.id + '_CL').className = ECMS_class_cache[node.expandedc];
	document.getElementById('ECMS_' + node.tree + '|' + node.id + '_CH').style.display = 'block';
}


// collapse a node
function ECMS_PF_collapse(node) {
	document.getElementById('ECMS_' + node.tree + '|' + node.id + '_CH').style.display = 'none';
	document.getElementById('ECMS_' + node.tree + '|' + node.id + '_CL').className = ECMS_class_cache[node.collapsedc];
	ECMS_PF_image_swap(node, 0);
}


// include an expand/collapse control image
function ECMS_PF_include_control_image(node) {
	var image;
	if (node.expanded) {
		image = node.expandedi;
	} else {
		image = node.collapsedi;
	}
	return ECMS_include_control_image(node, image);
}

// swap expand/collapse image
function ECMS_PF_image_swap(node, expand, image) {
	if (document.images) {
		if (!image) {
			image = document.images['ECMS_' + node.tree + '|' + node.id + '_IMG'];
			if (!image) {
				return;
			}
		}
		var i = ECMS_image_cache[(expand ? node.expandedi : node.collapsedi)];
		image.src = i.src;
		image.alt = node.name + ' ' + i.alt;
	}
}

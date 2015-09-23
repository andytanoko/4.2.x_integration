/*
	*	File:		ecms_system_default.js
	*	Name:		Expandable/Collapsible Menu System
	*	Date:		2000-11-27

	*	Author:		Ben Boyle
	*	Email:		bboyle@inspire.server101.com

	*	Platform:	all javascript platforms

	This default menu simple uses document.write() to create nested <UL> of
	all menu items, for browsers which cannot handle more dynamic elements.

*/


// create the menu
function ECMS_PF_create_menu(root) {
	document.write('<UL CLASS="ECMS_expanded">\n' + ECMS_PF_write_menu(root) + '</UL>\n');
}


// write a tree
function ECMS_PF_write_menu(node) {
	var text = ECMS_PF_write_node(node);

	if (node.child) {
		node.expanded = true;
		text += '<UL CLASS="ECMS_expanded">\n';
		text += ECMS_PF_write_menu(node.child);
		text += '</UL>\n';
	}
	if (node.next) {
		text += ECMS_PF_write_menu(node.next);
	}
	return text;
}


// write a node
function ECMS_PF_write_node(node) {
	var text = '<LI CLASS="' + (node.child ? 'ECMS_expanded' : 'ECMS_bullet') + '">';
	if (node.url) {
		text += '<A HREF="' + node.url + '"' + (node.target ? ' TARGET="' + node.target + '"' : '') + '>';
	}
	text += node.name;
	if (node.url) {
		text += '</A>';
	}
	text += '</LI>\n';
	return text;
}

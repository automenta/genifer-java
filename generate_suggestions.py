#!/usr/bin/python
# -*- coding: GB18030 -*-

## Convert lexicon organization to HTML table
## ================================================================

# Fact is, *every* node is associated with a list of suggestions.
# So we need some "collection" of lists indexed by headings
# Perhaps an associative array of normal indexed arrays in Javascript?

# The TreeTable do not need to store any non-heading entries.
# The ID's of TreeTable are for headings only.

import string

# text file input
f = open("synonym_forest_organization.txt", 'r')

# open HTML file for output
f2 = open("web/context-menu.htm", 'w')

id = 0
grand_parents = [0]
current_level = 0
previous_node = 0

for line in f:
	# determine indentation level
	level = 0
	for c in line:
		if c == '\t':
			level += 1
		else:
			break

	# from indentation -> determine parent
	if level > current_level:
		# parent is previous node
		parent = previous_node
		# add one more grand-parent
		grand_parents.append(previous_node)
	elif level < current_level:
		# parent is determined by level
		parent = grand_parents[level]
		# grand-parent is reset by level
		grand_parents = grand_parents[:(level + 1)]
	else:
		# parent is previous-node's parent = last grand-parent
		parent = grand_parents[-1]

	print grand_parents

	# write to HTML
	# except that non-heading rows will be written as plain text

	s = string.rstrip(string.lstrip(line))

	# generate ID
	id += 1

	# construct tree ID in Arboreal format, eg: "0/3/1/0"
	# id2 = ""
	# for node in grand_parents:
		# id2 += '/'
		# id2 += str(node)
	# f2.write('#' + id2[1:] + "\n")
	# print id2

	if level == 0:
		f2.write("<tr data-tt-branch='true' data-tt-id=\"n%d\">" % id)
	elif level == 1:
		f2.write("<tr data-tt-branch='true' data-tt-id=\"n%d\" data-tt-parent-id=\"n%d\">" % (id, parent))
	else:
		f2.write("<tr data-tt-id=\"n%d\" data-tt-parent-id=\"n%d\">" % (id, parent))

	f2.write("<td>%s</td></tr>" % s)

	# update variables
	previous_node = id
	current_level = level

f2.close()
f.close()

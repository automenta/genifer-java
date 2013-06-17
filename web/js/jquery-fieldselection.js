/***************************************************************************************
(C) 2008 Kishore Nallan for DesignShack
http://www.kishorelive.com
kishore.nc@gmail.com

Author's note:

The script below is a modified version of the jQuery fieldSelection plugin by Alex Brem
found at: http://blog.0xab.cd/?action=view&url=jquery-plugin-fieldselection

I have taken the liberty to build on original plugin to incorporate some additional 
features I wanted.
***************************************************************************************/

/*
 * jQuery plugin: fieldSelection - v0.1.0 - last change: 2006-12-16
 * (c) 2006 Alex Brem <alex@0xab.cd> - http://blog.0xab.cd
 *  modifications by Kishore on Oct 11, 2008  (kishore.nc@gmail.com) - http://www.kishorelive.com
 */

(function() {

	var fieldSelection = {

		getSelection: function() {

			var e = this.jquery ? this[0] : this;

			return (

				/* mozilla / dom 3.0 */
				('selectionStart' in e && function() {
					var l = e.selectionEnd - e.selectionStart;
					return { start: e.selectionStart, end: e.selectionEnd, length: l, text: e.value.substr(e.selectionStart, l) };
				}) ||

				/* exploder */
				(document.selection && function() {

					e.focus();

					var r = document.selection.createRange();
					if (r == null) {
						return { start: 0, end: e.value.length, length: 0 }
					}

					var re = e.createTextRange();
					var rc = re.duplicate();
					re.moveToBookmark(r.getBookmark());
					rc.setEndPoint('EndToStart', re);

					return { start: rc.text.length, end: rc.text.length + r.text.length, length: r.text.length, text: r.text };
				}) ||

				/* browser not supported */
				function() {
					return { start: 0, end: e.value.length, length: 0 };
				}

			)();

		},

		replaceSelection: function() {

			var e = this.jquery ? this[0] : this;
			var text = arguments[0] || '';

			return (

				/* mozilla / dom 3.0 */
				('selectionStart' in e && function() {
					
					var x = e.selectionStart; 
					if(text == "")
					{
						if(e.selectionStart == e.selectionEnd)  // backspace operation
						{
							e.value = e.value.substr(0, e.selectionStart - 1) + text + e.value.substr(e.selectionEnd, e.value.length);
							e.selectionStart = x - 1;   
							e.selectionEnd = x - 1;	
						}
						else {  // replace selected text
							e.value = e.value.substr(0, e.selectionStart) + text + e.value.substr(e.selectionEnd, e.value.length);						
							e.selectionStart = x;   
							e.selectionEnd = x;	
						}
					}
					
					else {
						e.value = e.value.substr(0, e.selectionStart) + text + e.value.substr(e.selectionEnd, e.value.length);
						e.selectionStart = x + 1;  
						e.selectionEnd = x + 1;	
					}
					return this;
				}) ||

				/* exploder */
				(document.selection && function() {
					
					e.focus();
					var range = document.selection.createRange();
					if(text == "")
					{
						
						if(range.text == "")
							range.moveStart("character", -1);
						range.text = text;
					}
					else
					{
							range.text = text;
							range.moveEnd("character", text.length - 1);
							range.select();
					}
					
					return this;
				}) ||

				/* browser not supported */
				function() {
					
					if(text=="")
					{
						var str = e.val();
						e.val( str.substring(0,str.length - 1 ) );				
					}
					else
						e.value += text;
					
					return this;
				}

			)();

		}

	};

	jQuery.each(fieldSelection, function(i) { jQuery.fn[i] = this; });

})();

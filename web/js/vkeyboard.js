/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/****************************************************************
(C) 2008 Kishore Nallan for DesignShack
http://www.kishorelive.com
kishore.nc@gmail.com
*****************************************************************/

$(document).ready(function(){

	var shifton = false;

	// toggles the keyboard to show or hide when link is clicked
	$("#showkeyboard").click(function(e) {
		var height = $('#keyboard').height();
		var width = $('#keyboard').width();
		leftVal=e.pageX-40+"px";
		topVal=e.pageY+20+"px";
		$('#keyboard').css({left:leftVal,top:topVal}).toggle();
	});

	// makes the keyboard draggable
	$("#keyboard").draggable();

	// toggles between the normal and the "SHIFT keys" on the keyboard
	function onShift(e) {
		var i;
		if(e==1) {
			for(i=0;i<4;i++) {
				var rowid = "#row" + i;
				$(rowid).hide();
				$(rowid+"_shift").show();
			}
		}
		else {
			for(i=0;i<4;i++) {
				var rowid = "#row" + i;
				$(rowid).show();
				$(rowid+"_shift").hide();
			}
		}
	 }

	// function thats called when any of the keys on the keyboard are pressed
	$("#keyboard input").bind("click", function(e) {

		if( $(this).val() == 'Backspace' ) {
			$('#pwd').replaceSelection("", true);
		}

		else if( $(this).val() == "Shift" ) {
			if(shifton == false) {
				onShift(1);
				shifton = true;
			}

			else {
				onShift(0);
				shifton = false;
			}
		}

		else {

			$('#pwd').replaceSelection($(this).val(), true);

			if(shifton == true) {
				onShift(0);
				shifton = false;
			}
		}
	});

});


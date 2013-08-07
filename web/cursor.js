/**
 * Where Is My Mouse?
 * Author: Royal Nason <http://inic.io>
 * 2013
 */

$.ajaxSetup({ cache: false });

function Mouse() {};

var mouse = new Mouse();

mouse.x = 100;
mouse.y = 100;  

Mouse.prototype.moveTo = function() {
	$('.cursor').css('top',mouse.y+'px');
	$('.cursor').css('left',mouse.x+'px');
};

function setPosition(){
	$.getJSON("http://connorcrawford.com/whereismymouse/mouse.json", function(data) {
		if(data != null){	
			mouse.x = data.x;
			mouse.y = data.y;
			mouse.moveTo();

			console.log(data);
			if(data.active == 1){
				$('.cursor').removeClass('beachball')
			} else {
				$('.cursor').addClass('beachball');
			}
		}
	})
	.error(function(e){
		console.log(e);
	})
	.complete(function(){
		setTimeout(setPosition,100);	
	});
}

$(document).ready(function(){ 
	setTimeout(setPosition, 300);
});
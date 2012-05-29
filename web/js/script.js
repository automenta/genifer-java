!function(angular) {

	var geniferApp = angular.module("geniferApp", []);

	geniferApp.service("pubsub", ["$rootScope", function($rootScope) {
		return {
		    pub: function(name, parameters) {
		        $rootScope.$emit(name, parameters);
		    },
		    sub: function(name, listener) {
		        $rootScope.$on(name, listener);
		    }
		};
	}]);

	geniferApp.controller("TokenController", ["$scope", "$http", "pubsub", function ($scope, $http) {

		var canvas = document.getElementById("canvas"),
			diagrams = [], timeout = 0;

		$scope.tokens = [];
		$scope.logic = "";
		$scope.formula = "";
	
		$scope.$on("updateFormula", function() {
			// Update the formula
			var postData;
			if((postData = formularize(diagrams)) != false) {
				clearTimeout(timeout);
				timeout = setTimeout(function() {
					$http({
						method: "POST",
						url: "/formularize",
						data: $.param(postData),
						headers: {"Content-Type": "application/x-www-form-urlencoded"}})
						.success(function(data) {
							$scope.formula = data;
						});
				}, 300);
			}
		});
	
		$scope.$watch("logic", function(newVal, oldVal) {
			var arr = [], prev,
				height = $(canvas).height(),
				pos = {	y: height/2 - 12, x: 32 };
		
			if (newVal) arr = newVal.split(" ");
		
			// Set the value to scope
			arr = $.grep(arr, function(n) { return n; });
			$scope.tokens = arr;

			for(var i = 0; i < arr.length; i++) {	
				token = arr[i];
				if(diagrams[i] == undefined) {
					diagrams[i] = new Diagram({
						canvas: canvas,
						token: token,
						onChange: function() { $scope.$emit("updateFormula"); }
					});
					diagrams[i].addEndPoint("#ccc");
					if(i > 0) {
						var prev = diagrams[i - 1];
						var prevPos = prev.getPosition();
						diagrams[i].connect(prev); // Connect to previous diagram
						pos.x = prevPos.x + prev.getWidth() + 64; // Calculate the offset
						pos.y = prevPos.y;
					}
					diagrams[i].setPosition(pos);
				}
				diagrams[i].text(token);
			}
		
			// Remove the last node if the number of tokens changed
			if(arr.length < diagrams.length) {
				var diagram;
				var offset = diagrams.length - arr.length;
				for(var i = 0; i < offset; i++) {
					diagram = diagrams.pop();
					diagram.remove();
				}
			}
		
			$scope.$emit("updateFormula");
		});
	}]);
}(angular);

!function($) {
	$().ready(function() {
		var layout = $("#tabs-2").layout();
		layout.sizePane("east", 300);
		$("#wrapper").tabs();
	});
}(jQuery);

// jsPlumb and Diagram
!function(widnow, jsPlumb) {
	
	jsPlumb.importDefaults({
			Endpoint : ["Dot", {radius:2}],
			HoverPaintStyle : {strokeStyle:"#fcd700", lineWidth: 4 },
			ConnectionOverlays : [
				[ "Arrow", { 
					location: 1,
					id: "arrow",
					width: 12,
					length: 12
				} ]
			],
			DragOptions : { cursor: "pointer", zIndex:2000 },
			PaintStyle : { strokeStyle: "#ccc" },
			EndpointStyle : { width:16, height:16, fillStyle: "#333", strokeStyle: "#333" },
			Anchors : ["TopCenter", "TopCenter"]
	});
	
	jsPlumb.bind("click", function(c) { // Click to detach
		for(var i = 0; i < c.endpoints.length; i++) {
			jsPlumb.deleteEndpoint(c.endpoints[i]);
		}
		jsPlumb.detach(c);		
	});
	
	jsPlumb.bind("jsPlumbConnection", function(c) { Diagram.onChange(c); });
	jsPlumb.bind("jsPlumbConnectionDetached", function(c) { Diagram.onChange(c); });
	
	Diagram = function(options) {
		
		var that = this;
		
		this.token = options.token || "";		
		this.canvas = options.canvas || $("#canvas");
		this.onChange = options.onChange;
		this.maxConnections = options.maxConnections || 1;
		this.el = $("<div></div>");
		this.ep = $("<div class='ep'></div>");
		this.label = $("<span></span>");
		this.id = "token" + Diagram.getLastID();

		this.el
			.attr("id", this.id)
			.addClass("ui-token window ui-draggble unselectable")
			.data("token", this.token)
			.append(this.label).append(this.ep)
			.appendTo(this.canvas);
		
		jsPlumb.draggable(this.id);
		jsPlumb.makeTarget(this.el, {
			dropOptions: { hoverClass:"dragHover" },
			anchor: "Continuous",
			beforeDrop: function(params) { // Prevent element connect to itself
				var sourceConnections = jsPlumb.getConnections({ source: params.sourceId });
				if(params.targetId == params.sourceId || sourceConnections.length >= that.maxConnections) {
					return false;
				}
				return true;
			}
		});
		
		Diagram.instances[this.id] = this;
	}

	Diagram.prototype.addEndPoint = function(color) {
		color = color || "#333";
	    $(".ep", this.el).each(function(i,e) {
			var p = $(e).parent();
			jsPlumb.makeSource($(e), {
				parent: p,
				anchor:"Continuous",
				connector: "Straight",
				connectorStyle:{ strokeStyle: color, lineWidth: 4 }
			});
		});
	};
	
	Diagram.prototype.connect = function(target) {
		if(target instanceof Diagram) {
			jsPlumb.connect({ source: this.id, target: target.id });
		}
		
		return this;
	};
	
	Diagram.prototype.getWidth = function() { return this.el.width(); };
	Diagram.prototype.getHeight = function() { return this.el.height(); };
	
	Diagram.prototype.setPosition = function(pos) {
		var x = 0, y = 0;
		if(arguments.length > 1) {
			x = arguments[0];
			y = arguments[1];
		} else if(typeof(pos) == "object"){
			x = pos.x || 0;
			y = pos.y || 0;
		}
		
		this.el.css({ top: y, left: x });
		Diagram.repaint();
		
		return this;
	};
	
	Diagram.prototype.getPosition = function() {
		var offset = this.el.offset();
		return {x: offset.left, y: offset.top};
	};
	
	Diagram.prototype.text = function(text) {
		this.token = text;
		this.label.text(text);
		Diagram.repaint();
		
		return this;
	};
	
	Diagram.prototype.remove = function() {
		var c = [],
			t = jsPlumb.getConnections({source: this.id}),
			s = jsPlumb.getConnections({source: this.id});
		c = $.merge(t,s);
		for(var i = 0; i < c.length; i++) {
			var eps = c[i].endpoints;
			for(var j = 0; j < eps.length; j++) {
				jsPlumb.deleteEndpoint(eps[j]);
			}
			jsPlumb.detach(c[i]);
		}
		Diagram.instances[this.id] = null;
		delete Diagram.instances[this.id];
		this.el.remove();
		
		delete this;
	};
	
	Diagram.repaint = function() {
		jsPlumb.repaintEverything();
		
		return this;
	};
	
	Diagram.onChange = function(c) {
		var instance = Diagram.instances[c.sourceId];
		if(typeof(instance.onChange) != "function") {
			return;
		}
		instance.onChange.call(instance, c);
	};
	
	// Static properties
	
	Diagram.lastID = 0;
	Diagram.getLastID = function() { return Diagram.lastID++; };
	Diagram.instances = {};
	
	window.Diagram = Diagram;

}(window, jsPlumb);

// Generate a logic formula from an NL graph
function formularize(diagrams) {
	var links = [],
		words = [],
		data = false,
		elem, conns, diagramIDs = {};
	for(var i = 0; i < diagrams.length; i++) {
		var diagram = diagrams[i];
		diagramIDs[diagram.id] = i;
	}

	for(var i = 0; i < diagrams.length; i++) {

		words.push(diagrams[i].token);
		
		conns = jsPlumb.getConnections({ source: diagrams[i].id });
		
		if (conns[0] == null)
			links.push("-1");
		else
			links.push(diagramIDs[conns[0].targetId]);
	}
	
	if(links.length && words.length) {
		data = {c: links.join(","), d: words.join(",")};
	}

	return data;
}

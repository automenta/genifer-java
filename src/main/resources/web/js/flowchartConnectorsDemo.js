;(function() {

	window.jsPlumbDemo = {
		init : function() {
				
			jsPlumb.importDefaults({
				// default drag options
				DragOptions : { cursor: 'pointer', zIndex:2000 },
				// default to blue at one end and green at the other
				EndpointStyles : [{ fillStyle:"transparent" }, { fillStyle:"transparent" }],
				// blue endpoints 7 px; green endpoints 11.
				Endpoints : [ [ "Dot", {radius:10} ], [ "Dot", {radius:10} ]]
				// the overlays to decorate each connection with.  note that the label overlay uses a function to generate the label text; in this
				// case it returns the 'labelText' member that we set on each connection in the 'init' method below.
				//ConnectionOverlays : [
				//	[ "Arrow", { location:0.9 } ],
				//	[ "Label", {
				//		location:0.1,
				//		id:"label",
				//		cssClass:"aLabel"
				//	}]
				//]
			});			

			// this is the paint style for the connecting lines..
			var connectorPaintStyle = {
				lineWidth:3,
				strokeStyle:"red",
				joinstyle:"round"
			},
			// .. and this is the hover style. 
			connectorHoverStyle = {
				lineWidth:5,
				strokeStyle:"pink"
			},
			// the definition of source endpoints (the small blue ones)
			sourceEndpoint = {
				endpoint:[ "Dot", { radius:10 } ],
				paintStyle:{ fillStyle:"transparent"},
				isSource:true,
				connector:[ "Straight", { stub:40 } ],
				connectorStyle:connectorPaintStyle,
				hoverPaintStyle:connectorHoverStyle,
				connectorHoverStyle:connectorHoverStyle,
                dragOptions:{}
                //overlays:[
                //	[ "Label", {
	            //    	location:[0.5, 1.5],
	            //    	label:"Drag",
	            //    	cssClass:"endpointSourceLabel"
	            //  } ]
				//  ]
			},
			// a source endpoint that sits at BottomCenter
			bottomSource = jsPlumb.extend( { anchor:"BottomCenter" }, sourceEndpoint),
			// the definition of target endpoints (will appear when the user drags a connection) 
			targetEndpoint = {
				endpoint:"Dot",
				paintStyle:{ fillStyle:"red" },
				hoverPaintStyle:connectorHoverStyle,
				maxConnections:-1,
				dropOptions:{ hoverClass:"hover", activeClass:"active" },
				isTarget:true			
                //overlays:[
                //	[ "Label", { location:[0.5, -0.5], label:"Drop", cssClass:"endpointTargetLabel" } ]
                //]
			},
			init = function(connection) {
				connection.getOverlay("label").setLabel(connection.sourceId.substring(6) + "-" + connection.targetId.substring(6));
			};

			var allSourceEndpoints = [], allTargetEndpoints = [];
				_addEndpoints = function(toId, sourceAnchors, targetAnchors) {
					for (var i = 0; i < sourceAnchors.length; i++) {
						var sourceUUID = toId + sourceAnchors[i];
						allSourceEndpoints.push(jsPlumb.addEndpoint(toId, sourceEndpoint, { anchor:sourceAnchors[i], uuid:sourceUUID }));
					}
					for (var j = 0; j < targetAnchors.length; j++) {
						var targetUUID = toId + targetAnchors[j];
						allTargetEndpoints.push(jsPlumb.addEndpoint(toId, targetEndpoint, { anchor:targetAnchors[j], uuid:targetUUID }));
					}
				};

			// _addEndpoints("window1", [[0.5,0.5,0,0]], [[0.5,0.0,0,0]]);

			// listen for new connections; initialise them the same way we initialise the connections at startup.
			jsPlumb.bind("jsPlumbConnection", function(connInfo, originalEvent) { 
				init(connInfo.connection);
			});

			// make all the window divs draggable						
			// jsPlumb.draggable(jsPlumb.getSelector(".window"));

			// connect a few up
			var common = {
				anchors:[ [0.5,0.5,0,0], [0.5,0.0,0,0] ],
				endpoints:["Dot", "Dot" ],
				connector:["Straight"],
				paintStyle:{ strokeStyle:"red", lineWidth:3 }
				};
			// jsPlumb.connect({source:"window1", target: "window2"},common);
			// jsPlumb.connect({uuids:["window3BottomCenter", "window1BottomCenter"]});

			jsPlumb.repaintEverything();

			//
			// listen for clicks on connections, and offer to delete connections on click.
			//
			jsPlumb.bind("click", function(conn, originalEvent) {
				if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
					jsPlumb.detach(conn); 
			});			
		}
	};
})();


function deleteObjectConfirm(link,targetUrl) {
  var objectType = link.dataset.objectType;
  var objectId = link.dataset.objectId;
  var objectName = link.dataset.objectName;
 
 if(objectName == 'delete')
   if (window.confirm("Are you sure you want to delete "+ objectType+ "?")) {
    var deleteUrl = targetUrl  + objectId;
    
			$.ajax({
				 type : "GET",
				 contentType: "application/json",
				 url : deleteUrl,
				 success : function () {
					 alert( objectType + " deleted succesfully!");
					 location.reload();
				 },
			     error : function () {
					 alert("Cannot delete " + objectType + " due to a dependency conflict.");
			    	 location.reload();
			      }
				});
			}
		}


function pushShipmentConfirm(link,targetUrl) {
  var objectId = link.dataset.objectId;
  var objectName = link.dataset.objectName;
  
  if (objectName == 'push'){
    if (window.confirm("Are you sure you want to push shipment " + objectId + " to auction?")) {
      var pushUrl = targetUrl  + objectId;
    
			$.ajax({
				 type : "GET",
				 contentType: "application/json",
				 url : pushUrl,
				 success : function () {
					 alert("Shipment " + objectId + " pushed to auction");
					 location.reload();
				 },
			     error : function () {
					 alert("Cannot push shipment " + objectId + " to auction." );
			    	 location.reload();
			      }
				});
			}
		}
}

function auctionRemoveConfirm(link,targetUrl) {
  var objectId = link.dataset.objectId;
  var objectName = link.dataset.objectName;
  
  if (objectName == 'remove'){
    if (window.confirm("Are you sure you want to remove shipment "+ objectId+ " from auction?")) {
      var auctionRemoveUrl = targetUrl  + objectId;
    
			$.ajax({
				 type : "GET",
				 contentType: "application/json",
				 url : auctionRemoveUrl,
				 success : function () {
					 alert("Shipment " + objectId + " removed from auction");
					 location.reload();
				 },
			     error : function () {
					 alert("Cannot remove shipement " + objectId );
			    	 location.reload();
			      }
				});
			}
		}
}

function freezeShipmentConfirm(link,targetUrl) {
  var objectType = link.dataset.objectType;
  var objectId = link.dataset.objectId;
  var objectName = link.dataset.objectName;
  
  if (objectName == 'freeze'){
    if (window.confirm("Are you sure you want to freeze shipment "+ objectType)) {
      var freezeUrl = targetUrl  + objectId;
    
			$.ajax({
				 type : "GET",
				 contentType: "application/json",
				 url : freezeUrl,
				 success : function () {
					 alert( " Shipment " + objectId + " is now frozen");
					 location.reload();
				 },
			     error : function () {
					 alert("Cannot freeze shipment" + objectId );
			    	 location.reload();
			      }
				});
			}
		}
}

function unfreezeShipmentConfirm(link,targetUrl) {
  var objectId = link.dataset.objectId;
  var objectName = link.dataset.objectName;
  
  if (objectName == 'unfreeze'){
    if (window.confirm("Are you sure you want to unfreeze shipment "+ objectId)) {
      var unfreezeUrl = targetUrl  + objectId;
    
			$.ajax({
				 type : "GET",
				 contentType: "application/json",
				 url : unfreezeUrl,
				 success : function () {
					 alert( " Shipment " + objectId + " is now unfrozen");
					 location.reload();
				 },
			     error : function () {
					 alert("Cannot freeze shipment" + objectId );
			    	 location.reload();
			      }
				});
			}
		}
}

function resetBidConfirm(link,targetUrl) {
  var objectId = link.dataset.objectId;
  var objectName = link.dataset.objectName;
  
  if (objectName == 'reset'){
    if (window.confirm("Are you sure you want to reset the bid on shipment id "+ objectId + "?")) {
      var resetUrl = targetUrl  + objectId;
    
			$.ajax({
				 type : "GET",
				 contentType: "application/json",
				 url : resetUrl,
				 success : function () {
					 alert( " Bid for shipment " + objectId + " is reset ");
					 location.reload();
				 },
			     error : function () {
					 alert("Cannot reset shipment bid " + objectId );
			    	 location.reload();
			      }
				});
			}
		}
}

function endAuctionConfirm(link,targetUrl) {
  var objectId = link.dataset.objectId;
  var objectName = link.dataset.objectName;
  var objectType = link.dataset.objectType;
  
  if (objectName == 'force'){
    if (window.confirm("Are you sure you want to end the auction for shipment id "+ objectId + "?")) {
      var forceUrl = targetUrl
    
			$.ajax({
				 type : "GET",
				 contentType: "application/json",
				 url : forceUrl + objectId,
				 success : function () {
					 alert( " Shipment " + objectId + " auction ended.");
					 location.reload();
				 },
			     error : function () {
					 alert("Cannot force shipment" + objectId + " out of auction." );
			    	 location.reload();
			      }
				});
			}
		}
}


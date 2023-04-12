function deleteObjectConfirm(link,targetUrl) {
  var objectType = link.dataset.objectType;
  var objectId = link.dataset.objectId;
  //var objectName = link.dataset.objectName;

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
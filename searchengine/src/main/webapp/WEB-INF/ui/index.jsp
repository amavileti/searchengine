<%@ page contentType="text/html; charset=ISO-8859-1" %>
<jsp:include page="imports.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>

 <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
 <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
 <script>
 function updateRank(pageId){
	 url="http://localhost:8080/searchengine/updaterank/"+pageId;
	 $.getJSON(url, function(data){});
 }
 $(document).ready(function(){
	 
   $("#sbutton").click(function(){
     $("#results").empty();
     url="http://localhost:8080/searchengine/search/" + $("#searchText").val();

     $.getJSON(url,function(data){
       var items = [];
	   $.each(data, function() {
	   	 	     items.push('<a href="' + this.url + '" onclick=updateRank("' + this.pageId + '") target="_new">' + this.description + '</a><br/>');
  		});
  	$('<ul/>', {
	    'class': 'my-new-list',
	    html: items.join('')
  }).appendTo('#results');
     });
  });
  });
 </script>
</head>
	<body>
		<input type="text" maxlength="70" size="80" id="searchText" name="searchText" value="Enter keyword" />
		<button id="sbutton" name="sbutton" value="Search"/>Search</button>
		<p id="results" />
	</body>
</html>

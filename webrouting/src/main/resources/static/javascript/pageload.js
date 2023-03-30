//This script runs on every pageload, and sets up the navbar.
		function nav_open() {
			document.getElementById("content").style.marginLeft = "15%";
			document.getElementById("mySidebar").style.width = "15%";			
			document.getElementById("mySidebar").style.display = "block";
			document.getElementById("hamburger").style.display = "none";
		}
		
		function nav_close() {
		    document.getElementById("content").style.marginLeft = "0%";
		    document.getElementById("mySidebar").style.display = "none";
		    document.getElementById("hamburger").style.display = "block";
		}
		
				/* When the user clicks on the button,
		toggle between hiding and showing the dropdown content */
		function showDropdown() {
		  document.getElementById("shipmentsDropdown").classList.toggle("show");
		}

		// Close the dropdown menu if the user clicks outside of it
		window.onclick = function(event) {
		  if (!event.target.matches('.dropbtn') && !event.target.matches('.dropbtn_selected')) {
		    var dropdowns = document.getElementsByClassName("dropdown-content");
		    var i;
		    for (i = 0; i < dropdowns.length; i++) {
		      var openDropdown = dropdowns[i];
		      if (openDropdown.classList.contains('show')) {
		        openDropdown.classList.remove('show');
		      }
		    }
		  }
		}
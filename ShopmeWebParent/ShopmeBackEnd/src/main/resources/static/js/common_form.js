$(document).ready(function() {

		//alert("common form work alert");
		$("#btn-cancel").on("click", function() {
			window.location = moduleURL;
			
		});
		
		$("#fileImage").change(function() {
			fileSize = this.files[0].size;
			if(fileSize > 102400) {
				this.setCustomValidity("You must choose an image less than 100KB!");
				this.reportValidity();
			} else {
				this.setCustomValidity("");
				showImageThumbnail(this);
			}
			 
		});
		
	});

	
	function showImageTest() {
		alert("Yesss");
	}

	
	function showImageThumbnail(fileInput) {
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#thumbnail").attr("src", e.target.result);
		};
		
		reader.readAsDataURL(file);
	}
	
	

	function showModalDialog(title, message) {
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$("#modalDialog").modal();
	}

	// function showModalDialog(title){
	// 	$("#modalTitle").text(title);
	// 	$("#modalDeailog").modal();
	// }
	
	function showErrorModal(message) {
		showModalDialog("Error", message);
	}
	
	function showWarningModal(message) {
		showModalDialog("Warning", message);
	}

	function alertTest() {
		alert("alert Test Function work!!");
	}
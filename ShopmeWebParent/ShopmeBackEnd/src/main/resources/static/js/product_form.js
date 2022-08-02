/**
 * 
 * 
 **/


	dropdownBrands = $("#brand");
	dropdownCategories = $("#category");
	
	$(document).ready(function () {
		
		$("#shortDescription").richText();
		$("#fullDescription").richText();
		
		dropdownBrands.on('change', function() {
			dropdownCategories.empty();
			getCategories();
		});
		
		getCategories();
		
		$("input[name='extraImage']").each(function(index) {
			//alert("index==",index);
			$(this).change(function() {
				alert("test==",index);
				showExtraImageThumbnail(this, index);
			});
		});
//		$("#extraImage1").change(function() {
//			
//			if(!checkFileSize(this)) {
//				return;
//			}
//			
//			showExtraImageThumbnail(this);
//			 
//		});
	});
	
	
	function showExtraImageThumbnail(fileInput, index) {
		//alert("second work=="+index);
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#extraThumbnail"+ index).attr("src", e.target.result);
		};
		
		reader.readAsDataURL(file);
		
		addNextExtraImageSection(index + 1);
	}
	
	
	function addNextExtraImageSection(index) {
		alert("Index==="+index);
		html = `<div class="col border m-3 p-2">
				<div><label>Extra Image #${index + 1}:</label></div>
				<div class="m-2">
					<img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" class="img-fluid" 
					src="${defaultImageThumbnailSrc}">
				</div>
				<div>
					<input type="file" id="extraImage${index}" name="extraImage${index}"
					onchange="showExtraImageThumbnail(this, ${index})"
					accept="image/png, image/jpeg">
				</div>
			</div>`;
	
		$("#divProductImages").append(html);
	}
	
	
	function getCategories() {
		brandId = dropdownBrands.val();
		url = brandModuleURL + "/" + brandId + "/categories";
		$.get(url, function(responseJson) {
			$.each(responseJson, function(index, category) {
				$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
				
			});
		});
		
	}
	
	function checkUnique(form) {
		
		proId = $("#id").val();
		proName = $("#name").val();
		csrfValue = $("input[name='_csrf']").val();
		url = "[[@{/products/check_uqiue}]]";
		
		params = {id: proId, name: proName, _csrf: csrfValue};
		
		$.post(url, params, function(respone){
			if (respone == "OK") {
				form.submit();
			} else if ( respone == "Duplicate") {
				showWarningModal("There is another product having the name "+ proName)
			}
			else {
				showErrorModal("Unknown response from server");
			}
		}).fail(function() {
			showErrorModal("Could not connect to the server");
		});
		
		return false;	
	}
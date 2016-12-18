$ ->
  $.get "/addresses", (addresses) ->
    $.each addresses, (index, address) ->
      output = "First Name #{address.firstName}, Last Name #{address.lastName}, Street Name #{address.street}, City #{address.city}, State #{address.state}, Postal Zip Code #{address.zip}, Telephone Number: #{address.telephone}"
      $("#addresses").append $("<li>").text output

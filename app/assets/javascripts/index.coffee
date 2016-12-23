window.readAddresses = ->
  $.get "/addresses", (addresses) ->
    $("[id ='addresses']").empty()
    list = []
    $.each addresses, (index, address) ->
      eventId = address.id
      event = address.event
      output = "First Name #{address.firstName}, Last Name #{address.lastName}, Street Name #{address.street}, City #{address.city}, State #{address.state}, Postal Zip Code #{address.zip}, Telephone Number: #{address.telephone}"
      if event isnt "CREATE"
        [del, eid] = event.split ' '
        list.push eid
      else
        if String(eventId) in list
          $("#addresses").append $("<div class=#{eventId}>").append $("<li>").append $("<strike>").text output
        else
          $("#addresses").append $("<div class=#{eventId}>").append $("<li>").text output
          $("div.#{eventId}").append $("<button>Delete</button>").click -> deleteAddress(eventId)
          $("div.#{eventId}").append $("<button>Edit</button>").click -> editAddress(address)

deleteAddress = (id)->
  $.post "/delete/#{id}"

editAddress = (addressObject) ->
  $("[id ='create']").hide()
  $("[id ='First Name']").val addressObject.firstName
  $("[id ='Last Name']").val addressObject.lastName
  $("[id ='Street']").val addressObject.street
  $("[id ='City']").val addressObject.city
  $("[id ='State']").val addressObject.state
  $("[id ='Zip']").val addressObject.zip
  $("[id ='Telephone']").val addressObject.telephone
  $(".buttons").append $("<button id='update' type='SUBMIT'>Update</button>").click -> deleteAddress(addressObject.id)
  $(".buttons").append $("<button id='clear' type='reset'>Clear</button>").click ->
    $("[id ='create']").show()
    $("[id ='update']").hide()
    $("[id ='clear']").hide()
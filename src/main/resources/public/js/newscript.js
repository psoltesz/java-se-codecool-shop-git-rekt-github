$(document).ready(function () {

    $('.cart-container').on('click', function (e) {

        $('.modal').toggleClass('active');

    });


    $('.menu-item').on('click', function (e) {
        var title = $(this).data('title');
        $('.shop-card').hide(300);
        if(title === "All"){
            $('.shop-card').show(300);
        } else {
            $('*[data-sup='+title+']').show(300);
        }

    });


    $(document).on('click', '.button', function(){
        event.preventDefault();
        var productId = $(this).data('product_id');
        $.ajax({
            url:'/addToCart?productId='+productId,
            type: "get",
            success: function(response){
                $('.cart-content').html(response);
            }
        });
    })

    $(document).on('focusout', '.quantity-input', function(){
        var name = $(this).data('name');
        var quantity = $(this).val();
        console.log(name);
        console.log(quantity);
        $.ajax({
            url:'/updateShoppingCart?productName='+name+'&quantity='+quantity,
            type: "get",
            success: function(response){
                console.log(response);
            }
        });
    })


    $('.cart-container').on('click', function (e) {

        $('.cart-modal').toggleClass('active');

    });

    $('.close-modal').on('click', function (e) {

        $('.cart-modal').removeClass('active');

    });
    $('.checkout-button').on('click', function (e) {

        window.location.replace("/checkout");

    });


    $(document).on('click', '.cart-container', function(){
        $('.cart-item-container').empty();
        $('body').css('overflow', 'hidden');
        $('')
        $.ajax({
            url:'/getCartContent',
            type: "get",
            success: function(data){
                var products = JSON.parse(data);
                for(var i = 0; i < products.length; i++){
                    var name = products[i].name;
                    var quantity = products[i].quantity;
                    var price = products[i].defaultPrice;
                    var cartItem = populate(name, quantity, price);
                    renderCartItems(cartItem);
                }
            }
        });
        $.ajax({
            url:'/getTotalPrice',
            type: "get",
            success: function(data){
                $('.cart-total').html(data);
            }
        });
        $.ajax({
            url:'/getTotalPrice',
            type: "get",
            success: function(data){
                $('.cart-total').html(data);
                }
        });
    })


    var populate = function(name, quantity, price) {
        var cartItem = `<div class="cart-item">
                        <div class="item-name">`+name+`</div>
                        <div class="item-quantity">quantity: <input type="text" class="quantity-input" data-name="` + name + `" value="`+ quantity +`"></input> </div>
                        <div class="item-price">price: `+price+`</div>
                        <i class="fa fa-trash" aria-hidden="true"></i>
                    </div>`
        return cartItem;
    }

    var renderCartItems = function(cartItem) {
        $('.cart-item-container').append(cartItem);
    }

    var checkCartSize = function(){
        $.ajax({
            url:'/getCartSize',
            type: "get",
            success: function(response){
                $('.cart-content').html(response);
            }
        });
    }

    checkCartSize();

})


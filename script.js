// ✅ Letters only (space allowed between words)
jQuery.validator.addMethod('lettersonly', function(value, element) {
    return this.optional(element) || /^[a-zA-Z\s-]+$/.test(value);
});

// ✅ No space allowed anywhere
jQuery.validator.addMethod('noSpace', function(value, element) {
    return this.optional(element) || /^\S+$/i.test(value);
});

// ✅ Numbers only
jQuery.validator.addMethod('numericOnly', function(value, element) {
    return this.optional(element) || /^[0-9]+$/.test(value);
});

// ✅ Address / general text (no starting space)
jQuery.validator.addMethod('all', function(value, element) {
    return this.optional(element) || /^[^\s][a-zA-Z0-9_,.\s-]+$/.test(value);
});


$(function() {

    // user register validation

    var $userRegister = $("#userRegister");

    $userRegister.validate({
        rules: {
            name: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                noSpace: true,
                email: true
            },
            mobileNumber: {
                required: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            password: {
                required: true,
                noSpace: true
            },
            cpassword: {
                required: true,
                noSpace: true,
                equalTo: '#pass'
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true,
                lettersonly: true
            },
            state: {
                required: true,
                lettersonly: true
            },
            pincode: {
                required: true,
                numericOnly: true,
                minlength: 6,
                maxlength: 6
            },
            img: {
                required: true
            }
        },

        messages: {
            name: {
                required: 'Name required',
                lettersonly: 'Only letters allowed'
            },
            email: {
                required: 'Email is required',
                noSpace: 'Space not allowed',
                email: 'Invalid email'
            },
            mobileNumber: {
                required: 'Mobile number required',
                numericOnly: 'Only digits allowed',
                minlength: 'Minimum 10 digits',
                maxlength: 'Maximum 12 digits'
            },
            password: {
                required: 'Password is required',
                noSpace: 'Space not allowed'
            },
            cpassword: {
                required: 'Confirm password required',
                noSpace: 'Space not allowed',
                equalTo: 'Password mismatch'
            },
            address: {
                required: 'Address required',
                all: 'Invalid address'
            },
            city: {
                required: 'City required',
                lettersonly: 'Only letters allowed'
            },
            state: {
                required: 'State required',
                lettersonly: 'Only letters allowed'
            },
            pincode: {
                required: 'Pincode required',
                numericOnly: 'Only digits allowed',
                minlength: 'Pincode must be 6 digits',
                maxlength: 'Pincode must be 6 digits'
            },
            img: {
                required: 'Image required'
            }
        }
    });

    var $resetPassword = $("#resetPassword");

    $resetPassword.validate({
        rules: {
            password: {
                required: true,
                noSpace: true
            },
            cpassword: {
                required: true,
                noSpace: true,
                equalTo: '#pass'
            }
        },
        messages: {
            password: {
                required: 'Password is required',
                noSpace: 'Space not allowed'
            },
            cpassword: {
                required: 'Confirm password required',
                noSpace: 'Space not allowed',
                equalTo: 'Password mismatch'
            }
        }
    })


    // ✅ Order Form Validation
    var $orders = $("#orders");

    $orders.validate({
        rules: {
            firstName: {
                required: true,
                lettersonly: true
            },
            lastName: {
                required: true,
                lettersonly: true
            },
            Email: {   // same as form
                required: true,
                noSpace: true,
                email: true
            },
            mobilrNo: {   // same as form (typo bhi same rakha)
                required: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true,
                lettersonly: true
            },
            State: {   // same as form
                required: true,
                lettersonly: true
            },
            pincode: {
                required: true,
                numericOnly: true,
                minlength: 6,
                maxlength: 6
            }
        },

        messages: {
            firstName: {
                required: "First name required",
                lettersonly: "Only letters allowed"
            },
            lastName: {
                required: "Last name required",
                lettersonly: "Only letters allowed"
            },
            Email: {
                required: "Email required",
                noSpace: "Space not allowed",
                email: "Invalid email"
            },
            mobilrNo: {
                required: "Mobile number required",
                numericOnly: "Only digits allowed",
                minlength: "Minimum 10 digits",
                maxlength: "Maximum 12 digits"
            },
            address: {
                required: "Address required",
                all: "Invalid address"
            },
            city: {
                required: "City required",
                lettersonly: "Only letters allowed"
            },
            State: {
                required: "State required",
                lettersonly: "Only letters allowed"
            },
            pincode: {
                required: "Pincode required",
                numericOnly: "Only digits allowed",
                minlength: "Must be 6 digits",
                maxlength: "Must be 6 digits"
            }
        }
    });



});
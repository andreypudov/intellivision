/* 
 * IntelliJustice Intelligent Referee Assistant System
 *
 * The MIT License
 *
 * Copyright 2011-2014 Andrey Pudov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * The collection of validation functions used in client side validation.
 *
 * @author    Andrey Pudov        <mail@andreypudov.com>
 * @version   0.00.00
 * %name      validation.js
 * %date      11:40:00 AM, Jan 06, 2014
 */

/**
 * Validates contacts form and returns true if form data is correct, and false
 * otherwise.
 * 
 * @returns {Boolean} the validation status.
 */
function validateContactForm() {
    var $nameField = $('#contactForm\\:contactInputName');
    
    return (validateText($nameField));
}

/**
 * Validates a value of input text field and if the value is emptry sets error
 * style class and returns false, otherwise return true.
 * 
 * @param {type} inputField the input text field.
 * @returns {Boolean}       true if text field falue is valid, false otherwise.
 */
function validateText(inputField) {
    var $field = $(inputField);
    var $group = $field.parent();
    var value = ($field.val() === '');
            
    $group.toggleClass('input-group has-error', value);
    
    return !value;
}
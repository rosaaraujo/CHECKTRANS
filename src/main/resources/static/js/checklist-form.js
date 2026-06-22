(function () {
    'use strict';

    var container = document.getElementById('items-container');
    var addButton = document.getElementById('add-item');

    if (!container || !addButton) return;

    var itemCount = container.querySelectorAll('.item-row').length;

    function createItemRow(index) {
        var div = document.createElement('div');
        div.className = 'row g-2 mb-2 item-row';

        div.innerHTML =
            '<div class="col-md-6">' +
                '<input type="text" name="items[' + index + '].description" class="form-control form-control-sm" placeholder="Descripci\u00f3n del elemento">' +
            '</div>' +
            '<div class="col-md-2">' +
                '<select name="items[' + index + '].isPass" class="form-select form-select-sm">' +
                    '<option value="">Seleccionar</option>' +
                    '<option value="true">Correcto</option>' +
                    '<option value="false">Incorrecto</option>' +
                '</select>' +
            '</div>' +
            '<div class="col-md-3">' +
                '<input type="text" name="items[' + index + '].observations" class="form-control form-control-sm" placeholder="Observaciones">' +
            '</div>' +
            '<div class="col-md-1">' +
                '<button type="button" class="btn btn-outline-danger btn-sm remove-item">' +
                    '<i class="fas fa-times"></i>' +
                '</button>' +
            '</div>' +
            '<input type="hidden" name="items[' + index + '].itemOrder" value="' + index + '">';

        return div;
    }

    function reindexItems() {
        var rows = container.querySelectorAll('.item-row');
        rows.forEach(function (row, idx) {
            row.querySelectorAll('input, select').forEach(function (input) {
                var name = input.getAttribute('name');
                if (name) {
                    name = name.replace(/items\[\d+\]/, 'items[' + idx + ']');
                    input.setAttribute('name', name);
                }
                if (input.getAttribute('name') && input.getAttribute('name').indexOf('itemOrder') !== -1) {
                    input.value = idx;
                }
            });
        });
    }

    addButton.addEventListener('click', function () {
        var rows = container.querySelectorAll('.item-row');
        var newIndex = rows.length;
        var newRow = createItemRow(newIndex);
        container.appendChild(newRow);
    });

    container.addEventListener('click', function (e) {
        if (e.target && (e.target.classList.contains('remove-item') || e.target.closest('.remove-item'))) {
            var btn = e.target.classList.contains('remove-item') ? e.target : e.target.closest('.remove-item');
            var row = btn.closest('.item-row');
            if (row && container.querySelectorAll('.item-row').length > 1) {
                row.remove();
                reindexItems();
            }
        }
    });
})();

function parseTabTable(text) {
    if (!text) {
        return { headers: [], rows: [] };
    }
    var lines = text.trim().split(/\r?\n/);
    if (lines.length === 0 || lines[0].trim() === '') {
        return { headers: [], rows: [] };
    }
    var headers = lines[0].split(/\t/);
    var rows = [];
    for (var i = 1; i < lines.length; i++) {
        if (lines[i].trim() === '') {
            continue;
        }
        rows.push(lines[i].split(/\t/));
    }
    return { headers: headers, rows: rows };
}

function getColumnValues(data, name) {
    if (!data || !data.headers) {
        return [];
    }
    var idx = data.headers.indexOf(name);
    if (idx === -1) {
        return [];
    }
    var values = [];
    for (var i = 0; i < data.rows.length; i++) {
        values.push(data.rows[i][idx]);
    }
    return values;
}

function TableViewModel() {
    var self = this;
    self.input = ko.observable('');
    self.headers = ko.observableArray([]);
    self.rows = ko.observableArray([]);
    self.data = null;

    self.parse = function () {
        self.data = parseTabTable(self.input());
        self.headers(self.data.headers);
        self.rows(self.data.rows);
    };

    self.validate = function () {
        // not implemented
    };

    self.copyIsValid = function () {
        if (!self.data) {
            return;
        }
        var values = getColumnValues(self.data, 'IS_VALID');
        if (values.length > 0) {
            navigator.clipboard.writeText(values.join('\n'));
        }
    };
}

function createPayload(prompt) {
    return { prompt: prompt };
}

function preparePrompts(current, requests) {
    if (current.indexOf('[REQUEST]') === -1) {
        return [current];
    }
    var lines = (requests || '').split(/\r?\n/).filter(function (l) {
        return l.trim() !== '';
    });
    if (lines.length === 0) {
        return [current.replace('[REQUEST]', '')];
    }
    return lines.map(function (l) {
        return current.replace('[REQUEST]', l);
    });
}

function collectResponses(messages) {
    return messages.filter(function (m) {
        return m.from === 'Llama';
    }).map(function (m) {
        return m.text;
    }).join('\n');
}

function LlamaViewModel() {
    var self = this;
    self.messages = ko.observableArray([]);
    self.current = ko.observable("");
    self.requests = ko.observable("");

    self.send = function () {
        var prompts = preparePrompts(self.current(), self.requests());
        if (prompts.length === 0) {
            return;
        }
        self.current('');
        prompts.forEach(function (text) {
            self.messages.push({ from: 'You', text: text });
            $.post('chat', createPayload(text))
                .done(function (data) {
                    self.messages.push({ from: 'Llama', text: data });
                });
        });
    };

    self.copyResponses = function () {
        var text = collectResponses(self.messages());
        if (navigator.clipboard) {
            navigator.clipboard.writeText(text);
        }
    };

    self.downloadResponses = function () {
        var text = collectResponses(self.messages());
        var blob = new Blob([text], { type: 'text/plain' });
        var a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = 'responses.txt';
        a.click();
        URL.revokeObjectURL(a.href);
    };

    self.clean = function () {
        self.messages([]);
    };
}

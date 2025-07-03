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

function runSequentially(prompts, sendFn, progressFn, isInterrupted, doneFn) {
    var total = prompts.length;
    var index = 0;
    function next() {
        if (isInterrupted() || index >= total) {
            if (doneFn) {
                doneFn();
            }
            return;
        }
        var jq = sendFn(prompts[index]);
        jq.done(function () {
            index++;
            if (progressFn) {
                progressFn(index, total);
            }
            next();
        });
    }
    next();
}

function LlamaViewModel() {
    var self = this;
    self.messages = ko.observableArray([]);
    self.current = ko.observable("");
    self.requests = ko.observable("");
    self.sending = ko.observable(false);
    self.progress = ko.observable(0);
    self._cancel = false;

    self.send = function () {
        var prompts = preparePrompts(self.current(), self.requests());
        if (prompts.length === 0) {
            return;
        }
        self.current('');
        self.sending(true);
        self.progress(0);
        self._cancel = false;
        runSequentially(
            prompts,
            function (text) {
                self.messages.push({ from: 'You', text: text });
                return $.post('chat', createPayload(text)).done(function (data) {
                    self.messages.push({ from: 'Llama', text: data });
                });
            },
            function (done, total) {
                self.progress(Math.round(done * 100 / total));
            },
            function () { return self._cancel; },
            function () { self.sending(false); }
        );
    };

    self.interrupt = function () {
        self._cancel = true;
        self.sending(false);
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

function createPayload(prompt) {
    return { prompt: prompt };
}

function LlamaViewModel() {
    var self = this;
    self.messages = ko.observableArray([]);
    self.current = ko.observable("");

    self.send = function () {
        var text = self.current();
        if (!text) {
            return;
        }
        self.messages.push({ from: 'You', text: text });
        self.current('');
        $.post('chat', createPayload(text))
            .done(function (data) {
                self.messages.push({ from: 'Llama', text: data });
            });
    };
}

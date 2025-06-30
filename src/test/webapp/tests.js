QUnit.test('createPayload wraps prompt', function(assert) {
    var p = createPayload('hello');
    assert.equal(p.prompt, 'hello');
});

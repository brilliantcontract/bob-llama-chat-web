QUnit.test('createPayload wraps prompt', function(assert) {
    var p = createPayload('hello');
    assert.equal(p.prompt, 'hello');
});

QUnit.test('parseTabTable returns headers and rows', function(assert) {
    var text = 'A\tB\n1\t2';
    var data = parseTabTable(text);
    assert.deepEqual(data.headers, ['A', 'B']);
    assert.deepEqual(data.rows, [['1', '2']]);
});

QUnit.test('getColumnValues extracts column by name', function(assert) {
    var data = { headers: ['X', 'IS_VALID'], rows: [['a', '1'], ['b', '0']] };
    var result = getColumnValues(data, 'IS_VALID');
    assert.deepEqual(result, ['1', '0']);
});

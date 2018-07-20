package dk.treenoter.adapter

class DataAdapter {
    String subject
    String text
    String id

    String createNewId() {
        id = UUID.randomUUID().toString()
    }

    HashMap<String, String> convertToHashmap() {
        final HashMap<String, String> hashmap = new HashMap<String, String>()
        hashmap.put('id', id)
        hashmap.put('subject', subject)
        hashmap.put('text', text)
        hashmap
    }
    
    static buildFromHashmap(HashMap<String, String> map) {
        assert map.get('id')
        assert map.get('subject')
        
        new DataAdapter(id: map.get('id'), 
                subject: map.get('subject'),
                text: map.get('text'))
    }
}

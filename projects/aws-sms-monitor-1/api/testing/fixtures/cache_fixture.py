"""
    This is a mock that represents a fake Redis cache
"""

class Cache: 
    data = {}

    def set(self, key, val, ex=None):
        self.data[key] = val

    def get(self, key):
        return self.data.get(key)

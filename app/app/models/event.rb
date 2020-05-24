class Event < ApplicationRecord
  has_rich_text :description
  acts_as_taggable_on :tags

  belongs_to :user
end

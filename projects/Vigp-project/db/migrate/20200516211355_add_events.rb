class AddEvents < ActiveRecord::Migration[6.0]
  def change
    create_table :events do |t|
      t.string   :title,        null: false
      t.text     :description
      t.datetime :date
      t.integer  :max_user
      t.string   :status
      t.string   :tags
      t.string   :discord_url
      t.belongs_to :user, null: false
    end
  end
end

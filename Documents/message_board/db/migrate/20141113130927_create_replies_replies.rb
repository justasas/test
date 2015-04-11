class CreateRepliesReplies < ActiveRecord::Migration
  def change
    create_table :replies_replies, id: false do |t|
      t.belongs_to :reply
      t.belongs_to :reply
    end
  end
end

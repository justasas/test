class AddIndexToTable < ActiveRecord::Migration
  def change
    add_index :accounts, :internet_forum_id
    add_index :categories, :internet_forum_id
    add_index :forums, :account_id
    add_index :forums, :category_id
    add_index :ratings, :topic_id
    add_index :ratings, :account_id
    add_index :replies, :account_id
    add_index :replies, :topic_id
    add_index :replies, :last_edit_by_id
    add_index :replies, :reply_id
    add_index :replies_replies, :reply_id
    add_index :topics, :account_id
    add_index :topics, :forum_id
  end
end

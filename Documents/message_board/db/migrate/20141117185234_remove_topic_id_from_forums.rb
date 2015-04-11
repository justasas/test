class RemoveTopicIdFromForums < ActiveRecord::Migration
  def change
    remove_column :forums, :topic_id, :integer
  end
end

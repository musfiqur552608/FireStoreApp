package org.freedu.firestoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.freedu.firestoreapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ItemClickListener {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val db = FirebaseFirestore.getInstance()
    private val storyCollection = db.collection("story")
    private val story = mutableListOf<Story>()
    private lateinit var storyAdapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        storyAdapter = StoryAdapter(story, this)
        binding.recyclerView.adapter = storyAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addbtn.setOnClickListener {
            val title = binding.titleEt.text.toString()
            val desc = binding.descEt.text.toString()
            if(title.isNotEmpty() && desc.isNotEmpty()){
                addStroy(title, desc)
            }else{
                Toast.makeText(this,"Please, fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        fetchData()
    }

    private fun fetchData() {
        storyCollection
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                story.clear()
                for(st in it){
                    val item = st.toObject(Story::class.java)
                    item.id = st.id
                    story.add(item)
                }
                storyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addStroy(title: String, desc: String) {
        val newStory = Story(title = title, description = desc, timeStamp = Timestamp.now())
        storyCollection.add(newStory)
            .addOnSuccessListener {
                newStory.id = it.id
                story.add(newStory)
                storyAdapter.notifyDataSetChanged()
                binding.titleEt.text = null
                binding.descEt.text = null
                fetchData()
                Toast.makeText(this,"Story added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Failed to add story", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onEditItemClick(story: Story) {
        binding.titleEt.setText(story.title)
        binding.descEt.setText(story.description)
        binding.addbtn.text = "Update"

        binding.addbtn.setOnClickListener {
            val updateTitle = binding.titleEt.text.toString()
            val updateDesc = binding.descEt.text.toString()
            if(updateTitle.isNotEmpty() && updateDesc.isNotEmpty()){
                val updateStory = Story(story.id, updateTitle, updateDesc,Timestamp.now())
                storyCollection.document(story.id!!)
                    .set(updateStory)
                    .addOnSuccessListener {
                        binding.titleEt.text?.clear()
                        binding.descEt.text?.clear()
                        Toast.makeText(this,"Story updated", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    }
            }else{
                Toast.makeText(this,"Please, fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDeleteItemClick(story: Story) {
        storyCollection.document(story.id!!)
            .delete()
            .addOnSuccessListener {
                storyAdapter.notifyDataSetChanged()
                fetchData()
                Toast.makeText(this,"Story deleted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed to delete story", Toast.LENGTH_SHORT).show()
            }
    }
}